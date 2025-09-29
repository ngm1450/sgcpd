import {Router} from '@angular/router';
import {PerfilStrategy} from './perfil-strategy.interface';
import {Perfil} from '../../shared/enums/perfil.enum';

export class AlunoStrategy implements PerfilStrategy {

  constructor(private router: Router) {}

  redirecionar() {
    void this.router.navigate([`/${Perfil.ALUNO.toLowerCase()}/pagina-inicial`]);
  }
}
