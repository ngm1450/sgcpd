import {Conteudo} from './conteudo.type';
import {Arquivo} from './arquivo.type';

export type ConteudoCompleto = Conteudo & { corpo: string; } & { idsTags: number[] } & { tagNames: number[] } & { arquivos: Arquivo[] };
