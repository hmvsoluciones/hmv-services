export interface ITemplate {
  id?: number;
  nombre?: string;
  contenidoMarkdown?: string;
  variables?: string | null;
  activo?: boolean | null;
}

export class Template implements ITemplate {
  constructor(
    public id?: number,
    public nombre?: string,
    public contenidoMarkdown?: string,
    public variables?: string | null,
    public activo?: boolean | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
