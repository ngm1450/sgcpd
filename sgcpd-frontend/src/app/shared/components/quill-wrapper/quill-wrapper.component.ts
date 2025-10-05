import { Component, forwardRef, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import {ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent, QuillModules} from 'ngx-quill';

@Component({
  selector: 'quill-wrapper',
  templateUrl: './quill-wrapper.component.html',
  styleUrls: ['./quill-wrapper.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => QuillWrapperComponent),
      multi: true,
    },
  ],
  imports: [
    QuillEditorComponent,
    FormsModule
  ]
})
// Adicionamos OnChanges para detectar mudanças no @Input()
export class QuillWrapperComponent implements ControlValueAccessor, OnChanges {

  // ✅ NOVO: Input para receber um valor simples (não-reativo)
  @Input() value: string = '';

  // ✅ NOVO: Output para emitir as mudanças para o binding (valueChange)
  @Output() valueChange = new EventEmitter<string>();

  // O conteúdo interno do editor (agora chamado de 'content' para unificar)
  content: string = '';

  isDisabled: boolean = false;

  @Input() placeholder: string = 'Digite o texto aqui...';
  @Input() modules: QuillModules = {
    history: {
      delay: 2500,
      userOnly: true,
    },
    toolbar: [
      [{ 'font': [] }, { 'size': ['small', false, 'large', 'huge'] }],
      [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'script': 'sub'}, { 'script': 'super' }],
      ['blockquote', 'code-block'],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'indent': '-1'}, { 'indent': '+1' }],
      [{ 'direction': 'rtl' }],
      [{ 'align': [] }],
      ['link', 'image', 'video'],
      ['clean'],
    ],
  };

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  /**
   * ✅ NOVO: Detecta mudanças vindas de fora quando [value] é usado.
   * Se o valor passado pelo @Input mudar, atualizamos o conteúdo interno.
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['value'] && changes['value'].currentValue !== this.content) {
      this.content = changes['value'].currentValue;
    }
  }

  /**
   * (ControlValueAccessor) Escreve o valor vindo do FormControl.
   */
  writeValue(value: any): void {
    this.content = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  /**
   * ✅ ATUALIZADO: Quando o conteúdo muda, propaga a mudança para AMBOS os canais.
   * - Chama o onChange para o FormControl.
   * - Emite o valueChange para o binding [(value)].
   */
  onContentChanged(event: any): void {
    const newContent = event.html === null ? '' : event.html;
    this.content = newContent;

    // Propaga para o FormControl
    this.onChange(newContent);
    // Propaga para o binding [(value)]
    this.valueChange.emit(newContent);
  }

  onBlur(): void {
    this.onTouched();
  }
}
