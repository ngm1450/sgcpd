import {StatusConteudoEnum} from '../enums/status-conteudo.enum';

export type Conteudo = {
  id: number;
  titulo: string;
  status: StatusConteudoEnum;
  nomeAutor?: string;
  nomeCategoria?: string
  autor: string;
  categoria: string;
  dataCriacao: Date;
  dataAtualizacao: Date;
}
