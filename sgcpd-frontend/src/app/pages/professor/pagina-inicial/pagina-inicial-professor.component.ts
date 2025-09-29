import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { AuthService } from '../../../core/auth/auth.service';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: false,
  selector: 'pagina-inicial-professor',
  templateUrl: './pagina-inicial-professor.component.html',
  styleUrls: ['./pagina-inicial-professor.component.scss']
})
export class PaginaInicialProfessorComponent implements OnInit {
  professor: any;

  @ViewChild('modalListaAlunos')
  modalListaAlunos!: TemplateRef<any>;

  modalRef!: NgbModalRef;


  turmaSelecionada: any;

  alunosTurma: any[] = [];

  alunosPorTurma = [
    {
      idTurma: 27,
      alunos: [
        { nome: 'Aline Rocha', matricula: 'A20250004', situacao: 'Ativo' },
        { nome: 'Bruno Castro', matricula: 'A20250007', situacao: 'Reprovado por Falta' },
        { nome: 'Larissa Alves', matricula: 'A20250011', situacao: 'Ativo' },
        { nome: 'Tiago Moreira', matricula: 'A20250018', situacao: 'Trancado' }
      ]
    },
    {
      idTurma: 32,
      alunos: [
        { nome: 'Carlos Souza', matricula: 'A20250002', situacao: 'Ativo' },
        { nome: 'Fernanda Lima', matricula: 'A20250003', situacao: 'Aprovado com dependência' },
        { nome: 'Gustavo Martins', matricula: 'A20250008', situacao: 'Ativo' },
        { nome: 'Juliana Freitas', matricula: 'A20250014', situacao: 'Trancado' }
      ]
    },
    {
      idTurma: 35,
      alunos: [
        { nome: 'Bruna Carvalho', matricula: 'A20250001', situacao: 'Ativo' },
        { nome: 'Rafael Dias', matricula: 'A20250019', situacao: 'Reprovado por Nota' },
        { nome: 'Luana Ribeiro', matricula: 'A20250009', situacao: 'Ativo' },
        { nome: 'Renato Silveira', matricula: 'A20250022', situacao: 'Ativo' }
      ]
    }
  ]

  constructor(private authService: AuthService, private modalService: NgbModal) {}

  ngOnInit(): void {
    const { nome, matricula } = this.authService.getUsuarioLogado();
    this.professor = {
      nome: `${nome} - ${matricula}`,
      departamento: 'Instituto de Informática (INF)',
      fotoPerfil: 'assets/fernanda_lima.png',
      totalTurmas: 3,
      totalAlunos: 12,
      lancamentosPendentes: 5,
      turmas: [
        { id: 27, codigoDisciplina: 'INF101', nomeDisciplina: 'Inteligência Computacional', horario: 'Seg/Qua 08h-10h' },
        { id: 32, codigoDisciplina: 'INF201', nomeDisciplina: 'Mineração de dados', horario: 'Ter/Qui 10h-12h' },
        { id: 35, codigoDisciplina: 'INF301', nomeDisciplina: 'Aprendizado de Máquina', horario: 'Sex 14h-16h' }
      ]
    };
  }

  abrirModal(idTurmaSelecionada: number) {
    const { codigoDisciplina, nomeDisciplina } = this.professor.turmas.find(({ id: idTurma }: { id: number }) => idTurmaSelecionada === idTurma) || {}
    this.turmaSelecionada = {
      codigoDisciplina,
      nomeDisciplina,
      alunos: this.alunosPorTurma.find(({ idTurma }) => idTurma === idTurmaSelecionada)?.alunos || []
    }
    this.modalRef = this.modalService.open(this.modalListaAlunos, { centered: true, size: 'lg', backdrop: 'static' });
  }

}
