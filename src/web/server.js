/*
Simple Node.js server with CORS headers for Home Assistant dashboard
Run: node server.js
Then open: http://localhost:5500/homeassistant-dashboard.html
*/

const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 5500;

// MIME types
const mimeTypes = {
    '.html': 'text/html',
    '.js': 'application/javascript',
    '.css': 'text/css',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.gif': 'image/gif',
    '.svg': 'image/svg+xml',
    '.ico': 'image/x-icon'
};

// Add CORS headers
function addCORSHeaders(res) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    res.setHeader('Access-Control-Allow-Credentials', 'true');
}

// Handle requests
const server = http.createServer((req, res) => {
    // Add CORS headers to all responses
    addCORSHeaders(res);

    // Handle OPTIONS preflight requests
    if (req.method === 'OPTIONS') {
        res.writeHead(200);
        res.end();
        return;
    }

    // Parse URL
    let url = req.url;
    if (url === '/') {
        url = '/homeassistant-dashboard.html';
    }

    // Get file path
    const filePath = path.join(__dirname, url);
    const ext = path.extname(filePath).toLowerCase();
    const contentType = mimeTypes[ext] || 'text/plain';

    // Serve file
    fs.readFile(filePath, (err, content) => {
        if (err) {
            if (err.code === 'ENOENT') {
                res.writeHead(404, { 'Content-Type': 'text/html' });
                res.end('<h1>404 - File Not Found</h1><p>Try: <a href="/homeassistant-dashboard.html">Dashboard</a></p>');
            } else {
                res.writeHead(500, { 'Content-Type': 'text/html' });
                res.end('<h1>500 - Server Error</h1><p>' + err.message + '</p>');
            }
            return;
        }

        res.writeHead(200, { 
            'Content-Type': contentType,
            'Content-Length': Buffer.byteLength(content)
        });
        res.end(content);
    });
});

// Start server
server.listen(PORT, () => {
    console.log('🌐 Server running on http://localhost:' + PORT);
    console.log('📱 Open: http://localhost:' + PORT + '/homeassistant-dashboard.html');
    console.log('🔧 CORS headers enabled - no more connection errors!');
    console.log('❌ Press Ctrl+C to stop server');
});

// Handle graceful shutdown
process.on('SIGINT', () => {
    console.log('\n🛑 Server stopped');
    server.close(() => {
        process.exit(0);
    });
});
