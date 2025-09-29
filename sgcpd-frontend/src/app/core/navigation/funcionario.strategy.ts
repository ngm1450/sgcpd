import {Router} from '@angular/router';
import {PerfilStrategy} from './perfil-strategy.interface';
import {Perfil} from '../../shared/enums/perfil.enum';

export class FuncionarioStrategy implements PerfilStrategy {

  constructor(private router: Router) {}

  redirecionar() {
    void this.router.navigate([`/${Perfil.FUNCIONARIO.toLowerCase()}/pagina-inicial`]);
  }
}
