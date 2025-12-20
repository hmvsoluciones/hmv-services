export interface ITemplate {
  id?: number;
  nombre?: string;
  contenido?: string;
  activo?: boolean | null;
}

export class Template implements ITemplate {
  constructor(
    public id?: number,
    public nombre?: string,
    public contenido?: string,
    public activo?: boolean | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
