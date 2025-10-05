import {StatusConteudoEnum} from '../enums/status-conteudo.enum';

export type ConteudoCriacao = {
  titulo: string;
  corpo: string;
  idCategoria: number;
  idsTags: number[];
  status: StatusConteudoEnum;
}
