import {AlunoStrategy} from './aluno.strategy';
import {Perfil} from '../../shared/enums/perfil.enum';
import {ProfessorStrategy} from './professor-strategy';
import {FuncionarioStrategy} from './funcionario.strategy';
import {Router} from '@angular/router';
import {PerfilStrategy} from './perfil-strategy.interface';

export class PerfilStrategyFactory {
  constructor(private router: Router) {}

  getStrategy(perfil: string): PerfilStrategy  {
    switch (perfil) {
      case Perfil.ALUNO: return new AlunoStrategy(this.router);
      case Perfil.PROFESSOR: return new ProfessorStrategy(this.router);
      case Perfil.FUNCIONARIO: return new FuncionarioStrategy(this.router);
      default:
        return { redirecionar: () => this.router.navigate(['/pagina-inicial']) };
    }
  }
}
