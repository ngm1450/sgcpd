import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent {
  @Input({ required: true }) open = false;

  @Input() title = 'Confirmar ação';
  @Input() message = 'Tem certeza?';
  @Input() confirmLabel = 'Confirmar';
  @Input() cancelLabel = 'Cancelar';

  @Input() danger = false;

  @Input() busy = false;

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onBackdropClick() { if (!this.busy) this.cancel.emit(); }
  stop(e: Event) { e.stopPropagation(); }
  onKeydown(e: KeyboardEvent) {
    if (this.busy) return;
    if (e.key === 'Escape') this.cancel.emit();
  }
}
