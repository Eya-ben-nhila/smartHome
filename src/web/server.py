#!/usr/bin/env python3
"""
Simple HTTP server to serve Home Assistant dashboard with CORS headers
Run: python server.py
Then open: http://localhost:5500/homeassistant-dashboard.html
"""

from http.server import HTTPServer, SimpleHTTPRequestHandler
import urllib.parse
import json

class CORSHTTPRequestHandler(SimpleHTTPRequestHandler):
    def end_headers(self):
        # Add CORS headers
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        self.send_header('Access-Control-Allow-Credentials', 'true')
        super().end_headers()

    def do_OPTIONS(self):
        self.send_response(200)
        self.end_headers()

    def do_GET(self):
        # Parse the path
        parsed_path = urllib.parse.urlparse(self.path)
        path = parsed_path.path.lstrip('/')
        
        if path == '':
            # Redirect to dashboard
            self.send_response(302)
            self.send_header('Location', '/homeassistant-dashboard.html')
            self.end_headers()
            return
        
        try:
            # Serve the requested file
            if path.endswith('.html'):
                content_type = 'text/html'
            elif path.endswith('.js'):
                content_type = 'application/javascript'
            elif path.endswith('.css'):
                content_type = 'text/css'
            else:
                content_type = 'text/plain'
            
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            self.send_response(200)
            self.send_header('Content-Type', content_type)
            self.end_headers()
            self.wfile.write(content.encode('utf-8'))
            
        except FileNotFoundError:
            self.send_response(404)
            self.send_header('Content-Type', 'text/html')
            self.end_headers()
            self.wfile.write(b'<h1>404 - File Not Found</h1>')
        except Exception as e:
            self.send_response(500)
            self.send_header('Content-Type', 'text/html')
            self.end_headers()
            self.wfile.write(f'<h1>500 - Server Error</h1><p>{str(e)}</p>'.encode())

def run_server():
    port = 5500
    server = HTTPServer(('localhost', port), CORSHTTPRequestHandler)
    
    print(f"🌐 Starting server on http://localhost:{port}")
    print(f"📱 Open: http://localhost:{port}/homeassistant-dashboard.html")
    print("🔧 This server adds CORS headers to avoid connection errors")
    print("❌ Press Ctrl+C to stop server")
    
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        print("\n🛑 Server stopped")
        server.server_close()

if __name__ == '__main__':
    run_server()
