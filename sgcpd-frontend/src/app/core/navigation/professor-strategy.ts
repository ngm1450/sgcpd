import {Router} from '@angular/router';
import {Perfil} from '../../shared/enums/perfil.enum';
import {PerfilStrategy} from './perfil-strategy.interface';

export class ProfessorStrategy implements PerfilStrategy {

  constructor(private router: Router) {}

  redirecionar() {
    void this.router.navigate([`/${Perfil.PROFESSOR.toLowerCase()}/pagina-inicial`]);
  }
}
