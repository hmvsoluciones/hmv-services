export interface INegocio {
  id?: number;
  nombre?: string;
  responsable?: string;
  celular?: string | null;
  correo?: string | null;
  subscriptionKey?: string;
  esActivo?: boolean | null;
}

export class Negocio implements INegocio {
  constructor(
    public id?: number,
    public nombre?: string,
    public responsable?: string,
    public celular?: string | null,
    public correo?: string | null,
    public subscriptionKey?: string,
    public esActivo?: boolean | null,
  ) {
    this.esActivo = this.esActivo ?? false;
  }
}
