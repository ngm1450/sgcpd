import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AuthService} from '../../../core/auth/auth.service';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  standalone: false,
  selector: 'pagina-inicial-funcionario',
  templateUrl: './pagina-inicial-funcionario.component.html',
  styleUrls: ['./pagina-inicial-funcionario.component.scss']
})
export class PaginaInicialFuncionarioComponent implements OnInit {

  funcionario: any;

  formCadastroAluno: FormGroup;

  alunosCadastrados: any[] = [];

  cursos = [
    'Engenharia de Software',
    'Matemática Aplicada',
    'Ciência da Computação'
  ];


  @ViewChild('modalCadastroAluno')
  modalCadastroAluno!: TemplateRef<any>;

  modalCadastroAlunoRef!: NgbModalRef;

  @ViewChild('modalInicializaoPeriodo')
  modalInicializaoPeriodo!: TemplateRef<any>;

  modalInicializaoPeriodoRef!: NgbModalRef;

  constructor(private authService: AuthService,
              private modalService: NgbModal,
              formBuilder: FormBuilder) {
    this.formCadastroAluno = formBuilder.group({
      nome: ['', Validators.required],
      cpf: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefone: [''],
      sexo: ['', Validators.required],
      matricula: ['', Validators.required],
      situacao: ['Ativo'],
      limiteCreditos: [200],
      curso: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const { nome, matricula } = this.authService.getUsuarioLogado();
    this.funcionario = {
      nome: `${nome} - ${matricula}`,
      cargo: 'Coordenador Acadêmico',
      departamento: 'Instituto de Informática (INF)',
      fotoPerfil: 'assets/pedro_matos.png',
      stats: {
        alunosAtivos: 2,
        turmasAbertas: 10,
        pendenciasMatricula: 0,
        solicitacoesRecentes: 0
      }
    };
  }

  abrirCadastroAluno() {
    this.modalCadastroAlunoRef = this.modalService.open(this.modalCadastroAluno, { centered: true });
  }

  salvarAluno() {
    if (this.formCadastroAluno.invalid) return;

    const novoAluno = this.formCadastroAluno.value;
    this.alunosCadastrados.push(novoAluno);

    this.modalCadastroAlunoRef.close();
    this.formCadastroAluno.reset();
  }

  abrirPeriodoMatricula() {
    this.modalInicializaoPeriodoRef = this.modalService.open(this.modalInicializaoPeriodo, { centered: true });
  }
}
