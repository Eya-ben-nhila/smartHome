import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminUser } from '../../../services/admin.service';

type DialogMode = 'create' | 'edit';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrl: './user-dialog.component.scss'
})
export class UserDialogComponent {
  mode: DialogMode = this.data?.mode ?? 'create';

  form = this.fb.group({
    fullName: [this.data?.user?.fullName ?? this.getSuggestedName(), [Validators.required]],
    email: [this.data?.user?.email ?? '', [Validators.required, Validators.email]],
    password: [''],
    role: [this.normalizeRole(this.data?.user?.role) ?? 'EMPLOYEE', [Validators.required]],
    enabled: [this.data?.user?.enabled ?? true],
    profileImageUrl: [this.data?.user?.profileImageUrl ?? '']
  });

  suggestedNames = [
    'Emma Johnson',
    'Noah Martin',
    'Sophia Brown',
    'Liam Garcia',
    'Olivia Lee',
    'Lucas Wilson'
  ];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mode: DialogMode; user?: AdminUser }
  ) {
    if (this.mode === 'create') {
      this.form.controls.password.setValidators([Validators.required, Validators.minLength(6)]);
    } else {
      this.form.controls.email.disable();
    }
    this.form.controls.password.updateValueAndValidity();
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.dialogRef.close(this.form.getRawValue());
  }

  fillSampleIdentity(): void {
    const name = this.getSuggestedName();
    const slug = name.toLowerCase().replace(/\s+/g, '.');
    this.form.patchValue({
      fullName: name,
      email: `${slug}@smartmonitor.com`
    });
  }

  setGeneratedAvatar(): void {
    const fullName = this.form.controls.fullName.value?.trim() || this.getSuggestedName();
    const avatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(fullName)}&background=2196f3&color=fff&size=256`;
    this.form.patchValue({ profileImageUrl: avatar });
  }

  private normalizeRole(role: AdminUser['role'] | undefined): 'ADMIN' | 'EMPLOYEE' {
    return role === 'ADMIN' ? 'ADMIN' : 'EMPLOYEE';
  }

  private getSuggestedName(): string {
    return this.suggestedNames[Math.floor(Math.random() * this.suggestedNames.length)];
  }
}
