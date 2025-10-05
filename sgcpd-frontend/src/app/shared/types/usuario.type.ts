export type Usuario = {
  id: number;
  nome: string;
  email: string;
  ativo: boolean;
  dataCriacao?: Date;
  dataAtualizacao?: Date;
}
