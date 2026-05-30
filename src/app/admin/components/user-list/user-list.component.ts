import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AdminService, AdminUser, CreateUserPayload, UpdateUserPayload } from '../../../services/admin.service';
import { UserDialogComponent } from '../user-dialog/user-dialog.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  displayedColumns = ['fullName', 'email', 'role', 'enabled', 'actions'];
  users: AdminUser[] = [];
  loading = false;
  readonly realNames = [
    'Emma Johnson',
    'Noah Martin',
    'Sophia Brown',
    'Liam Garcia',
    'Olivia Lee',
    'Lucas Wilson',
    'Ava Thomas',
    'Ethan Taylor'
  ];

  constructor(private adminService: AdminService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.adminService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  addUser(): void {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      width: '520px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe((value: CreateUserPayload | undefined) => {
      if (!value) return;
      const payload: CreateUserPayload = {
        ...value,
        profileImageUrl: value.profileImageUrl || this.avatarUrl(
          { id: '', fullName: value.fullName, email: value.email, role: value.role, enabled: value.enabled },
          0
        )
      };
      this.adminService.createUser(payload).subscribe(() => this.loadUsers());
    });
  }

  editUser(user: AdminUser): void {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      width: '520px',
      data: { mode: 'edit', user }
    });

    dialogRef.afterClosed().subscribe((value: any) => {
      if (!value) return;
      const payload: UpdateUserPayload = {
        fullName: value.fullName,
        role: value.role,
        enabled: value.enabled,
        password: value.password || undefined,
        profileImageUrl: value.profileImageUrl || undefined
      };
      this.adminService.updateUser(user.id, payload).subscribe(() => this.loadUsers());
    });
  }

  disableUser(user: AdminUser): void {
    this.adminService.disableUser(user.id).subscribe(() => this.loadUsers());
  }

  displayName(user: AdminUser, index: number): string {
    if (!user.fullName) return this.realNames[index % this.realNames.length];
    const isPlaceholder = /test|new user|jane|john|mobile/i.test(user.fullName);
    return isPlaceholder ? this.realNames[index % this.realNames.length] : user.fullName;
  }

  avatarUrl(user: AdminUser, index: number): string {
    if (user.profileImageUrl) return user.profileImageUrl;
    const seed = encodeURIComponent(this.displayName(user, index) || user.email || user.id);
    return `https://api.dicebear.com/9.x/adventurer/svg?seed=${seed}`;
  }
}
