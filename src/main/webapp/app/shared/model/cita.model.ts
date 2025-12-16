import { type ICliente } from '@/shared/model/cliente.model';
import { type IEmpleado } from '@/shared/model/empleado.model';

import { type EstadoCita } from '@/shared/model/enumerations/estado-cita.model';
export interface ICita {
  id?: number;
  fecha?: Date;
  horaInicio?: string;
  duracionMinutos?: number | null;
  descripcion?: string;
  esUrgente?: boolean | null;
  estadoCita?: keyof typeof EstadoCita | null;
  cliente?: ICliente;
  empleado?: IEmpleado | null;
}

export class Cita implements ICita {
  constructor(
    public id?: number,
    public fecha?: Date,
    public horaInicio?: string,
    public duracionMinutos?: number | null,
    public descripcion?: string,
    public esUrgente?: boolean | null,
    public estadoCita?: keyof typeof EstadoCita | null,
    public cliente?: ICliente,
    public empleado?: IEmpleado | null,
  ) {
    this.esUrgente = this.esUrgente ?? false;
  }
}
