import {ActivatedRouteSnapshot, CanActivate, Router, UrlTree} from '@angular/router';
import {AuthService} from './auth.service';
import {Perfil} from '../../shared/enums/perfil.enum';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PerfilGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  async canActivate(route: ActivatedRouteSnapshot): Promise<boolean | UrlTree> {
    const perfilEsperado = route.data['perfil'] as Perfil;
    const perfilAtual = this.authService.getPerfil();

    if (perfilAtual !== perfilEsperado) {
      const rotaCorrigida = `/${perfilAtual.toLowerCase()}/pagina-inicial`;
      return this.router.parseUrl(rotaCorrigida);
    }

    return true;
  }
}
