Demokat es una aplicación diseñada para grabar demos de tus ideas de manera organizada y divertida

Notas:
La mejor manera de probar esta app es en un móvil físico (aunque hay que cambiar el archivo de postgres para que te deje) ya que el micrófono del emulador no es el mejor.
Tener puesto el volumen

Instrucciones:
Se le debe dar permiso de micrófono a la app
Si la app se va a probar en un emulador se debe cambiar la ip a jdbc:postgresql://10.0.2.2/demokat
Si se va a probar en un movil físico la ip debe ser la de su ordenador y en el archivo pg_hba.conf de postgres habrá que añadir la ip de su ordenador y de su móvil ambas conectados a la misma red.
Este archivo se situa en: C:\Program Files\PostgreSQL\16\data\pg_hba.conf
Hay que añadir las ips debajo de: "# IPv4 local connections:"
Ejemplo:
host    all             all             192.168.137.22/24       trust
host    all             all             192.168.137.215/24      trust
pero con sus ips (móvil y pc ambos conectados a la misma red)
La base de datos está alojada localmente, por ello, a continuación presento el script de esta:
create database demokat
    with owner postgres;

create table public.usuario
(
    id_user    serial
        primary key,
    username   varchar(255) not null,
    contrasena varchar      not null,
    name       varchar,
    surname    varchar
);

alter table public.usuario
    owner to postgres;

create table public.notas
(
    id_notas serial
        primary key,
    notas    text,
    titulo   varchar(255),
    user_id  integer
        references public.usuario
);

alter table public.notas
    owner to postgres;

create table public.rec
(
    id_rec      serial
        primary key,
    rec         text,
    titulo      varchar(255),
    instrumento varchar(100),
    user_id     integer
        references public.usuario,
    fecha       timestamp
);

alter table public.rec
    owner to postgres;

create table public.cancion
(
    id_cancion serial
        primary key,
    titulo     varchar(255),
    user_id    integer
        references public.usuario
);

alter table public.cancion
    owner to postgres;

create table public.cancion_rec
(
    id_cancion_rec serial
        primary key,
    rec_id         integer
        references public.rec
            on delete cascade,
    notas_id       integer
        references public.notas
            on delete cascade,
    cancion_id     integer
        references public.cancion
            on delete cascade
);

alter table public.cancion_rec
    owner to postgres;

create function public.actualizar_rec_en_cancion() returns trigger
    language plpgsql
as
$$
BEGIN

    IF EXISTS (
        SELECT 1 FROM cancion_rec
        WHERE rec_id = NEW.rec_id
          AND cancion_id = NEW.cancion_id
    ) THEN
        RETURN NULL;
    END IF;


    DELETE FROM cancion_rec
    WHERE rec_id = NEW.rec_id
      AND cancion_id != NEW.cancion_id;

    RETURN NEW;
END;
$$;

alter function public.actualizar_rec_en_cancion() owner to postgres;

create trigger actualizar_rec_trigger
    before insert
    on public.cancion_rec
    for each row
execute procedure public.actualizar_rec_en_cancion();

create function public.actualizar_nota_en_cancion() returns trigger
    language plpgsql
as
$$
BEGIN


    IF EXISTS (
        SELECT 1 FROM cancion_rec
        WHERE notas_id = NEW.notas_id
          AND cancion_id = NEW.cancion_id
    ) THEN
        RETURN NULL;
    END IF;


    DELETE FROM cancion_rec
    WHERE notas_id = NEW.notas_id
      AND cancion_id != NEW.cancion_id;

    RETURN NEW;
END;
$$;

alter function public.actualizar_nota_en_cancion() owner to postgres;

create trigger actualizar_nota_trigger
    before insert
    on public.cancion_rec
    for each row
execute procedure public.actualizar_nota_en_cancion();

