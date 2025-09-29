import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AuthService} from '../../../core/auth/auth.service';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: false,
  selector: 'pagina-inicial-aluno',
  templateUrl: './pagina-inicial-aluno.component.html',
  styleUrls: ['./pagina-inicial-aluno.component.scss']
})
export class PaginaInicialAlunoComponent implements OnInit {

  aluno: any;

  @ViewChild('modalMatricula')
  modalMatricula!: TemplateRef<any>;

  modalRef!: NgbModalRef;

  filtro = '';

  disciplinas = [
    { codigo: 'INF103', nome: 'Banco de Dados' },
    { codigo: 'INF104', nome: 'Redes de Computadores' },
    { codigo: 'MAT203', nome: 'Probabilidade e Estatística' },
    { codigo: 'FIS202', nome: 'Mecânica' },
    { codigo: 'LET103', nome: 'Linguística' }
  ];

  constructor(
    private authService: AuthService,
    private router: Router,
    private modalService: NgbModal
  ) {}

  get progressoPercentual(): number {
    return (this.aluno.creditosConcluidos / this.aluno.creditosTotais) * 100;
  }

  ngOnInit(): void {
    const usuario = this.authService.getUsuarioLogado();
    const matricula = usuario?.matricula;
    if (matricula === 'A20250001') {
      this.aluno = {
        nome: 'Bruna Carvalho',
        semestreAtual: '2025/1',
        curso: 'Ciência da Computação',
        departamento: 'Departamento de Computação',
        creditosConcluidos: 23,
        creditosTotais: 200,
        cr: 8.3,
        previsaoFormatura: '2029/1',
        fotoPerfil: 'assets/bruna_carvalho.png',
        turmas: [
          { codigoDisciplina: 'INF102', nomeDisciplina: 'POO com Java', horario: 'Seg/Qua 10h-12h', status: 'Matriculado' },
          { codigoDisciplina: 'MAT202', nomeDisciplina: 'Álgebra Linear', horario: 'Ter/Qui 08h-10h', status: 'Matriculado' },
          { codigoDisciplina: 'INF201', nomeDisciplina: 'Estrutura de Dados', horario: 'Qua/Sex 14h-16h', status: 'Matriculado' },
          { codigoDisciplina: 'FIS101', nomeDisciplina: 'Física I', horario: 'Seg 16h-18h', status: 'Matriculado' }
        ]
      };
    } else if (matricula === 'A20250002') {
      this.aluno = {
        nome: 'Carlos Souza',
        semestreAtual: '2025/1',
        curso: 'Matemática Aplicada',
        departamento: 'Departamento de Matemática',
        creditosConcluidos: 16,
        creditosTotais: 180,
        cr: 7.9,
        previsaoFormatura: '2028/2',
        fotoPerfil: 'assets/carlos_souza.jpg',
        turmas: [
          { codigoDisciplina: 'MAT101', nomeDisciplina: 'Cálculo I', horario: 'Seg/Qua 08h-10h', status: 'Matriculado' },
          { codigoDisciplina: 'MAT202', nomeDisciplina: 'Álgebra Linear', horario: 'Ter/Qui 10h-12h', status: 'Matriculado' },
          { codigoDisciplina: 'FIS101', nomeDisciplina: 'Física I', horario: 'Sex 08h-10h', status: 'Matriculado' },
          { codigoDisciplina: 'INF101', nomeDisciplina: 'Introdução à Programação', horario: 'Qua 14h-16h', status: 'Matriculado' }
        ]
      };
    } else {
      this.aluno = {
        nome: usuario?.nome ?? 'Aluno',
        semestreAtual: '2025/1',
        curso: 'Curso Genérico',
        departamento: 'Departamento Genérico',
        creditosConcluidos: 100,
        creditosTotais: 200,
        cr: 7.5,
        previsaoFormatura: '2026/2',
        fotoPerfil: 'assets/carlos_souza.jpg',
        turmas: []
      };
    }
  }

  abrirModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.open(template, { centered: true, size: 'lg', backdrop: 'static' });
  }

  fecharModal() {
    if (this.modalRef) {
      this.modalRef.close();
    }
  }

  disciplinasFiltradas() {
    return this.disciplinas.filter(d =>
      (d.nome.toLowerCase().includes(this.filtro.toLowerCase()) ||
        d.codigo.toLowerCase().includes(this.filtro.toLowerCase())) &&
      !this.aluno.turmas.some(({ codigoDisciplina }: { codigoDisciplina: string; }) => codigoDisciplina === d.codigo)
    )
  }

  adicionarDisciplina(disciplina: any) {
    this.aluno.turmas.push({
      codigoDisciplina: disciplina.codigo,
      nomeDisciplina: disciplina.nome,
      horario: 'Seg/Qua 12h-14h',
      status: 'Em processamento'
    });
    this.fecharModal();
  }

}
