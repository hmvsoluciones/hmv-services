import { type ICita } from '@/shared/model/cita.model';

export interface IServicio {
  id?: number;
  fechaAtencion?: Date;
  contenido?: string;
  precio?: number | null;
  cita?: ICita;
}

export class Servicio implements IServicio {
  constructor(
    public id?: number,
    public fechaAtencion?: Date,
    public contenido?: string,
    public precio?: number | null,
    public cita?: ICita,
  ) {}
}
