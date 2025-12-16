export interface IEmpleado {
  id?: number;
  nombre?: string;
  celular?: string | null;
  especialidad?: string | null;
  activo?: boolean | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public nombre?: string,
    public celular?: string | null,
    public especialidad?: string | null,
    public activo?: boolean | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
