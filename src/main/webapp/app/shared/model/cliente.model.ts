export interface ICliente {
  id?: number;
  nombre?: string;
  celular?: string;
  correo?: string | null;
  identificacion?: string | null;
  direccion?: string | null;
  activo?: boolean | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public celular?: string,
    public correo?: string | null,
    public identificacion?: string | null,
    public direccion?: string | null,
    public activo?: boolean | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
