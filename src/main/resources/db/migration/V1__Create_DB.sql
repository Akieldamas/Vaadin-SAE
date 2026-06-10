drop index if exists AUTEUR_P ;
drop table if exists AUTEUR;
drop index if exists CONVIE2_FK;
drop index if exists CONVIE_FK;
drop index if exists CONVIE_PK;
drop table if exists AUTEUR_EVENEMENT;
drop index if exists EST2_FK;
drop index if exists EST_FK;
drop index if exists EST_PK;
drop table if exists AUTEUR_TYPE_AUTEUR;
drop index if exists HERITAGE_2_FK;
drop index if exists BIBLIOTHECAIRE_PK;
drop table if exists BIBLIOTHECAIRE;
drop index if exists POSSEDE_FK;
drop index if exists CREE_FK;
drop index if exists FOURNIS_FK;
drop index if exists DOCUMENT_PK;
drop table if exists DOCUMENT;
drop index if exists APPARTIENT2_FK;
drop index if exists APPARTIENT_FK;
drop index if exists APPARTIENT_PK;
drop table if exists DOCUMENT_GENRE_DOCUMENT;
drop index if exists EDITEUR_PK;
drop table if exists EDITEUR;
drop index if exists HERITAGE_1_FK;
drop index if exists EMPRUNTEUR_PK;
drop table if exists EMPRUNTEUR;
drop index if exists EMPRUNT2_FK;
drop index if exists EMPRUNT_FK;
drop index if exists EMPRUNT_PK;
drop table if exists EMPRUNTEUR_DOCUMENT;
drop index if exists EVENEMENT_PK;
drop table if exists EVENEMENT;
drop index if exists ORGANISE2_FK;
drop index if exists ORGANISE_FK;
drop index if exists ORGANISE_PK;
drop table if exists EVENEMENT_BIBLIOTHECAIRE;
drop index if exists FORMAT_PK;
drop table if exists FORMAT;
drop index if exists GENRE_DOCUMENT_PK;
drop table if exists GENRE_DOCUMENT;
drop index if exists HERITAGE_3_FK;
drop index if exists LIVRE_PK;
drop table if exists LIVRE;
drop index if exists HERITAGE_4_FK;
drop index if exists MEDIA_PK;
drop table if exists MEDIA;
drop index if exists TYPE_AUTEUR_PK;
drop table if exists TYPE_AUTEUR;
drop index if exists UTILISATEUR_PK;
drop table if exists UTILISATEUR;

/*==============================================================*/
/* Table : AUTEUR                                               */
/*==============================================================*/
create table AUTEUR (
   ID_AUTEUR            SERIAL               not null,
   NOM_AUTEUR           VARCHAR(60)          null,
   PRENOM               VARCHAR(60)          null,
   DATE_NAISSANCE       DATE                 null,
   DATE_MORT            DATE                 null,
   VILLE_NAISSANCE      VARCHAR(250)         null,
   LIEN_WIKIPEDIA       VARCHAR(200)         null,
   constraint PK_AUTEUR primary key (ID_AUTEUR)
);

/*==============================================================*/
/* Index : AUTEUR_PK                                            */
/*==============================================================*/
create unique index AUTEUR_PK on AUTEUR (
ID_AUTEUR
);

/*==============================================================*/
/* Table : AUTEUR_EVENEMENT                                     */
/*==============================================================*/
create table AUTEUR_EVENEMENT (
   ID_EVENEMENT         INT4                 not null,
   ID_AUTEUR            INT4                 not null,
   constraint PK_AUTEUR_EVENEMENT primary key (ID_EVENEMENT, ID_AUTEUR)
);

/*==============================================================*/
/* Index : CONVIE_PK                                            */
/*==============================================================*/
create unique index CONVIE_PK on AUTEUR_EVENEMENT (
ID_EVENEMENT,
ID_AUTEUR
);

/*==============================================================*/
/* Index : CONVIE_FK                                            */
/*==============================================================*/
create  index CONVIE_FK on AUTEUR_EVENEMENT (
ID_EVENEMENT
);

/*==============================================================*/
/* Index : CONVIE2_FK                                           */
/*==============================================================*/
create  index CONVIE2_FK on AUTEUR_EVENEMENT (
ID_AUTEUR
);

/*==============================================================*/
/* Table : AUTEUR_TYPE_AUTEUR                                   */
/*==============================================================*/
create table AUTEUR_TYPE_AUTEUR (
   ID_AUTEUR            INT4                 not null,
   ID_TYPE_AUTEUR       INT4                 not null,
   constraint PK_AUTEUR_TYPE_AUTEUR primary key (ID_AUTEUR, ID_TYPE_AUTEUR)
);

/*==============================================================*/
/* Index : EST_PK                                               */
/*==============================================================*/
create unique index EST_PK on AUTEUR_TYPE_AUTEUR (
ID_AUTEUR,
ID_TYPE_AUTEUR
);

/*==============================================================*/
/* Index : EST_FK                                               */
/*==============================================================*/
create  index EST_FK on AUTEUR_TYPE_AUTEUR (
ID_AUTEUR
);

/*==============================================================*/
/* Index : EST2_FK                                              */
/*==============================================================*/
create  index EST2_FK on AUTEUR_TYPE_AUTEUR (
ID_TYPE_AUTEUR
);

/*==============================================================*/
/* Table : BIBLIOTHECAIRE                                       */
/*==============================================================*/
create table BIBLIOTHECAIRE (
   ID_UTILISATEUR       INT4                 not null,
   ID_BIBLIOTHECAIRE    SERIAL               not null,
   LOGIN                VARCHAR(25)          null,
   MOT_DE_PASSE         VARCHAR(4000)        null,
   NOM_AUTEUR           VARCHAR(60)          null,
   PRENOM               VARCHAR(60)          null,
   constraint PK_BIBLIOTHECAIRE primary key (ID_UTILISATEUR, ID_BIBLIOTHECAIRE)
);

/*==============================================================*/
/* Index : BIBLIOTHECAIRE_PK                                    */
/*==============================================================*/
create unique index BIBLIOTHECAIRE_PK on BIBLIOTHECAIRE (
ID_UTILISATEUR,
ID_BIBLIOTHECAIRE
);

/*==============================================================*/
/* Index : HERITAGE_2_FK                                        */
/*==============================================================*/
create  index HERITAGE_2_FK on BIBLIOTHECAIRE (
ID_UTILISATEUR
);

/*==============================================================*/
/* Table : DOCUMENT                                             */
/*==============================================================*/
create table DOCUMENT (
   ID_DOCUMENT          SERIAL               not null,
   ID_AUTEUR            INT4                 not null,
   ID_FORMAT            INT4                 not null,
   ID_EDITEUR           INT4                 not null,
   LIEN_GIF             VARCHAR(250)         null,
   DESCRIPTION          VARCHAR(1000)        null,
   DATE_ACQUISITION     DATE                 null,
   CODE_EMPLACEMENT     VARCHAR(10)          null,
   CODE_ISBN            CHAR(13)             null,
   CODE_EMPRUNT         VARCHAR(20)          null,
   SPECIFICITE          VARCHAR(20)          null,
   TITRE                VARCHAR(250)         null,
   DATE_PUBLICATION     DATE                 null,
   DATE_CREATION        DATE                 null,
   DATE_MAJ             DATE                 null,
   constraint PK_DOCUMENT primary key (ID_DOCUMENT)
);

/*==============================================================*/
/* Index : DOCUMENT_PK                                          */
/*==============================================================*/
create unique index DOCUMENT_PK on DOCUMENT (
ID_DOCUMENT
);

/*==============================================================*/
/* Index : FOURNIS_FK                                           */
/*==============================================================*/
create  index FOURNIS_FK on DOCUMENT (
ID_EDITEUR
);

/*==============================================================*/
/* Index : CREE_FK                                              */
/*==============================================================*/
create  index CREE_FK on DOCUMENT (
ID_AUTEUR
);

/*==============================================================*/
/* Index : POSSEDE_FK                                           */
/*==============================================================*/
create  index POSSEDE_FK on DOCUMENT (
ID_FORMAT
);

/*==============================================================*/
/* Table : DOCUMENT_GENRE_DOCUMENT                              */
/*==============================================================*/
create table DOCUMENT_GENRE_DOCUMENT (
   ID_GENRE_DOCUMENT    INT4                 not null,
   ID_DOCUMENT          INT4                 not null,
   constraint PK_DOCUMENT_GENRE_DOCUMENT primary key (ID_GENRE_DOCUMENT, ID_DOCUMENT)
);

/*==============================================================*/
/* Index : APPARTIENT_PK                                        */
/*==============================================================*/
create unique index APPARTIENT_PK on DOCUMENT_GENRE_DOCUMENT (
ID_GENRE_DOCUMENT,
ID_DOCUMENT
);

/*==============================================================*/
/* Index : APPARTIENT_FK                                        */
/*==============================================================*/
create  index APPARTIENT_FK on DOCUMENT_GENRE_DOCUMENT (
ID_GENRE_DOCUMENT
);

/*==============================================================*/
/* Index : APPARTIENT2_FK                                       */
/*==============================================================*/
create  index APPARTIENT2_FK on DOCUMENT_GENRE_DOCUMENT (
ID_DOCUMENT
);

/*==============================================================*/
/* Table : EDITEUR                                              */
/*==============================================================*/
create table EDITEUR (
   ID_EDITEUR           SERIAL               not null,
   NOM_SOCIETE          VARCHAR(60)          null,
   ADRESSE              VARCHAR(200)         null,
   LIEN_SITE_WEB        VARCHAR(200)         null,
   LIEN_WIKIPEDIA       VARCHAR(200)         null,
   constraint PK_EDITEUR primary key (ID_EDITEUR)
);

/*==============================================================*/
/* Index : EDITEUR_PK                                           */
/*==============================================================*/
create unique index EDITEUR_PK on EDITEUR (
ID_EDITEUR
);

/*==============================================================*/
/* Table : EMPRUNTEUR                                           */
/*==============================================================*/
create table EMPRUNTEUR (
   ID_UTILISATEUR       INT4                 not null,
   ID_EMPRUNTEUR        SERIAL               not null,
   LOGIN                VARCHAR(25)          null,
   MOT_DE_PASSE         VARCHAR(4000)        null,
   NOM_AUTEUR           VARCHAR(60)          null,
   PRENOM               VARCHAR(60)          null,
   ADRESSE              VARCHAR(200)         null,
   EMAIL                VARCHAR(150)         null,
   DATE_NAISSANCE       DATE                 null,
   DATE_FIN_ABONNEMENT  DATE                 null,
   NUMERO_CARTE         CHAR(10)             null,
   constraint PK_EMPRUNTEUR primary key (ID_UTILISATEUR, ID_EMPRUNTEUR)
);

/*==============================================================*/
/* Index : EMPRUNTEUR_PK                                        */
/*==============================================================*/
create unique index EMPRUNTEUR_PK on EMPRUNTEUR (
ID_UTILISATEUR,
ID_EMPRUNTEUR
);

/*==============================================================*/
/* Index : HERITAGE_1_FK                                        */
/*==============================================================*/
create  index HERITAGE_1_FK on EMPRUNTEUR (
ID_UTILISATEUR
);

/*==============================================================*/
/* Table : EMPRUNTEUR_DOCUMENT                                  */
/*==============================================================*/
create table EMPRUNTEUR_DOCUMENT (
   ID_UTILISATEUR       INT4                 not null,
   ID_EMPRUNTEUR        INT4                 not null,
   ID_DOCUMENT          INT4                 not null,
   FIN_EMPRUNT          DATE                 null,
   PROLONGATION         BOOL                 null,
   FIN_PROLONGATION     DATE                 null,
   DEBUT_EMPRUNT        DATE                 null,
   constraint PK_EMPRUNTEUR_DOCUMENT primary key (ID_UTILISATEUR, ID_EMPRUNTEUR, ID_DOCUMENT)
);

/*==============================================================*/
/* Index : EMPRUNT_PK                                           */
/*==============================================================*/
create unique index EMPRUNT_PK on EMPRUNTEUR_DOCUMENT (
ID_UTILISATEUR,
ID_EMPRUNTEUR,
ID_DOCUMENT
);

/*==============================================================*/
/* Index : EMPRUNT_FK                                           */
/*==============================================================*/
create  index EMPRUNT_FK on EMPRUNTEUR_DOCUMENT (
ID_UTILISATEUR,
ID_EMPRUNTEUR
);

/*==============================================================*/
/* Index : EMPRUNT2_FK                                          */
/*==============================================================*/
create  index EMPRUNT2_FK on EMPRUNTEUR_DOCUMENT (
ID_DOCUMENT
);

/*==============================================================*/
/* Table : EVENEMENT                                            */
/*==============================================================*/
create table EVENEMENT (
   ID_EVENEMENT         SERIAL               not null,
   DESCRIPTION          VARCHAR(1000)        null,
   DATE_DEBUT           DATE                 null,
   DATE_FIN             DATE                 null,
   LIEU                 VARCHAR(150)         null,
   constraint PK_EVENEMENT primary key (ID_EVENEMENT)
);

/*==============================================================*/
/* Index : EVENEMENT_PK                                         */
/*==============================================================*/
create unique index EVENEMENT_PK on EVENEMENT (
ID_EVENEMENT
);

/*==============================================================*/
/* Table : EVENEMENT_BIBLIOTHECAIRE                             */
/*==============================================================*/
create table EVENEMENT_BIBLIOTHECAIRE (
   ID_UTILISATEUR       INT4                 not null,
   ID_BIBLIOTHECAIRE    INT4                 not null,
   ID_EVENEMENT         INT4                 not null,
   constraint PK_EVENEMENT_BIBLIOTHECAIRE primary key (ID_UTILISATEUR, ID_BIBLIOTHECAIRE, ID_EVENEMENT)
);

/*==============================================================*/
/* Index : ORGANISE_PK                                          */
/*==============================================================*/
create unique index ORGANISE_PK on EVENEMENT_BIBLIOTHECAIRE (
ID_UTILISATEUR,
ID_BIBLIOTHECAIRE,
ID_EVENEMENT
);

/*==============================================================*/
/* Index : ORGANISE_FK                                          */
/*==============================================================*/
create  index ORGANISE_FK on EVENEMENT_BIBLIOTHECAIRE (
ID_UTILISATEUR,
ID_BIBLIOTHECAIRE
);

/*==============================================================*/
/* Index : ORGANISE2_FK                                         */
/*==============================================================*/
create  index ORGANISE2_FK on EVENEMENT_BIBLIOTHECAIRE (
ID_EVENEMENT
);

/*==============================================================*/
/* Table : FORMAT                                               */
/*==============================================================*/
create table FORMAT (
   ID_FORMAT            SERIAL               not null,
   LONGUEUR             DECIMAL              null,
   LARGEUR              DECIMAL              null,
   POIDS                DECIMAL              null,
   constraint PK_FORMAT primary key (ID_FORMAT)
);

/*==============================================================*/
/* Index : FORMAT_PK                                            */
/*==============================================================*/
create unique index FORMAT_PK on FORMAT (
ID_FORMAT
);

/*==============================================================*/
/* Table : GENRE_DOCUMENT                                       */
/*==============================================================*/
create table GENRE_DOCUMENT (
   ID_GENRE_DOCUMENT    SERIAL               not null,
   NOM_AUTEUR           VARCHAR(60)          null,
   constraint PK_GENRE_DOCUMENT primary key (ID_GENRE_DOCUMENT)
);

/*==============================================================*/
/* Index : GENRE_DOCUMENT_PK                                    */
/*==============================================================*/
create unique index GENRE_DOCUMENT_PK on GENRE_DOCUMENT (
ID_GENRE_DOCUMENT
);

/*==============================================================*/
/* Table : LIVRE                                                */
/*==============================================================*/
create table LIVRE (
   ID_DOCUMENT          INT4                 not null,
   ID_LIVRE             SERIAL               not null,
   ID_AUTEUR            INT4                 null,
   ID_FORMAT            INT4                 null,
   ID_EDITEUR           INT4                 null,
   LIEN_GIF             VARCHAR(250)         null,
   DESCRIPTION          VARCHAR(1000)        null,
   DATE_ACQUISITION     DATE                 null,
   CODE_EMPLACEMENT     VARCHAR(10)          null,
   CODE_ISBN            CHAR(13)             null,
   CODE_EMPRUNT         VARCHAR(20)          null,
   SPECIFICITE          VARCHAR(20)          null,
   TITRE                VARCHAR(250)         null,
   DATE_PUBLICATION     DATE                 null,
   DATE_CREATION        DATE                 null,
   DATE_MAJ             DATE                 null,
   NB_PAGES             INT4                 null,
   constraint PK_LIVRE primary key (ID_DOCUMENT, ID_LIVRE)
);

/*==============================================================*/
/* Index : LIVRE_PK                                             */
/*==============================================================*/
create unique index LIVRE_PK on LIVRE (
ID_DOCUMENT,
ID_LIVRE
);

/*==============================================================*/
/* Index : HERITAGE_3_FK                                        */
/*==============================================================*/
create  index HERITAGE_3_FK on LIVRE (
ID_DOCUMENT
);

/*==============================================================*/
/* Table : MEDIA                                                */
/*==============================================================*/
create table MEDIA (
   ID_DOCUMENT          INT4                 not null,
   ID_MEDIA             SERIAL               not null,
   ID_AUTEUR            INT4                 null,
   ID_FORMAT            INT4                 null,
   ID_EDITEUR           INT4                 null,
   LIEN_GIF             VARCHAR(250)         null,
   DESCRIPTION          VARCHAR(1000)        null,
   DATE_ACQUISITION     DATE                 null,
   CODE_EMPLACEMENT     VARCHAR(10)          null,
   CODE_ISBN            CHAR(13)             null,
   CODE_EMPRUNT         VARCHAR(20)          null,
   SPECIFICITE          VARCHAR(20)          null,
   TITRE                VARCHAR(250)         null,
   DATE_PUBLICATION     DATE                 null,
   DATE_CREATION        DATE                 null,
   DATE_MAJ             DATE                 null,
   DUREE                DECIMAL              null,
   constraint PK_MEDIA primary key (ID_DOCUMENT, ID_MEDIA)
);

/*==============================================================*/
/* Index : MEDIA_PK                                             */
/*==============================================================*/
create unique index MEDIA_PK on MEDIA (
ID_DOCUMENT,
ID_MEDIA
);

/*==============================================================*/
/* Index : HERITAGE_4_FK                                        */
/*==============================================================*/
create  index HERITAGE_4_FK on MEDIA (
ID_DOCUMENT
);

/*==============================================================*/
/* Table : TYPE_AUTEUR                                          */
/*==============================================================*/
create table TYPE_AUTEUR (
   ID_TYPE_AUTEUR       SERIAL               not null,
   LABEL                VARCHAR(50)          null,
   constraint PK_TYPE_AUTEUR primary key (ID_TYPE_AUTEUR)
);

/*==============================================================*/
/* Index : TYPE_AUTEUR_PK                                       */
/*==============================================================*/
create unique index TYPE_AUTEUR_PK on TYPE_AUTEUR (
ID_TYPE_AUTEUR
);

/*==============================================================*/
/* Table : UTILISATEUR                                          */
/*==============================================================*/
create table UTILISATEUR (
   ID_UTILISATEUR       SERIAL               not null,
   LOGIN                VARCHAR(25)          null,
   MOT_DE_PASSE         VARCHAR(4000)        null,
   NOM_AUTEUR           VARCHAR(60)          null,
   PRENOM               VARCHAR(60)          null,
   constraint PK_UTILISATEUR primary key (ID_UTILISATEUR)
);

/*==============================================================*/
/* Index : UTILISATEUR_PK                                       */
/*==============================================================*/
create unique index UTILISATEUR_PK on UTILISATEUR (
ID_UTILISATEUR
);

alter table AUTEUR_EVENEMENT
   add constraint FK_AUTEUR_E_CONVIE_EVENEMEN foreign key (ID_EVENEMENT)
      references EVENEMENT (ID_EVENEMENT)
      on delete restrict on update restrict;

alter table AUTEUR_EVENEMENT
   add constraint FK_AUTEUR_E_CONVIE2_AUTEUR foreign key (ID_AUTEUR)
      references AUTEUR (ID_AUTEUR)
      on delete restrict on update restrict;

alter table AUTEUR_TYPE_AUTEUR
   add constraint FK_AUTEUR_T_EST_AUTEUR foreign key (ID_AUTEUR)
      references AUTEUR (ID_AUTEUR)
      on delete restrict on update restrict;

alter table AUTEUR_TYPE_AUTEUR
   add constraint FK_AUTEUR_T_EST2_TYPE_AUT foreign key (ID_TYPE_AUTEUR)
      references TYPE_AUTEUR (ID_TYPE_AUTEUR)
      on delete restrict on update restrict;

alter table BIBLIOTHECAIRE
   add constraint FK_BIBLIOTH_HERITAGE__UTILISAT foreign key (ID_UTILISATEUR)
      references UTILISATEUR (ID_UTILISATEUR)
      on delete restrict on update restrict;

alter table DOCUMENT
   add constraint FK_DOCUMENT_CREE_AUTEUR foreign key (ID_AUTEUR)
      references AUTEUR (ID_AUTEUR)
      on delete restrict on update restrict;

alter table DOCUMENT
   add constraint FK_DOCUMENT_FOURNIS_EDITEUR foreign key (ID_EDITEUR)
      references EDITEUR (ID_EDITEUR)
      on delete restrict on update restrict;

alter table DOCUMENT
   add constraint FK_DOCUMENT_POSSEDE_FORMAT foreign key (ID_FORMAT)
      references FORMAT (ID_FORMAT)
      on delete restrict on update restrict;

alter table DOCUMENT_GENRE_DOCUMENT
   add constraint FK_DOCUMENT_APPARTIEN_GENRE_DO foreign key (ID_GENRE_DOCUMENT)
      references GENRE_DOCUMENT (ID_GENRE_DOCUMENT)
      on delete restrict on update restrict;

alter table DOCUMENT_GENRE_DOCUMENT
   add constraint FK_DOCUMENT_APPARTIEN_DOCUMENT foreign key (ID_DOCUMENT)
      references DOCUMENT (ID_DOCUMENT)
      on delete restrict on update restrict;

alter table EMPRUNTEUR
   add constraint FK_EMPRUNTE_HERITAGE__UTILISAT foreign key (ID_UTILISATEUR)
      references UTILISATEUR (ID_UTILISATEUR)
      on delete restrict on update restrict;

alter table EMPRUNTEUR_DOCUMENT
   add constraint FK_EMPRUNTE_EMPRUNT_EMPRUNTE foreign key (ID_UTILISATEUR, ID_EMPRUNTEUR)
      references EMPRUNTEUR (ID_UTILISATEUR, ID_EMPRUNTEUR)
      on delete restrict on update restrict;

alter table EMPRUNTEUR_DOCUMENT
   add constraint FK_EMPRUNTE_EMPRUNT2_DOCUMENT foreign key (ID_DOCUMENT)
      references DOCUMENT (ID_DOCUMENT)
      on delete restrict on update restrict;

alter table EVENEMENT_BIBLIOTHECAIRE
   add constraint FK_EVENEMEN_ORGANISE_BIBLIOTH foreign key (ID_UTILISATEUR, ID_BIBLIOTHECAIRE)
      references BIBLIOTHECAIRE (ID_UTILISATEUR, ID_BIBLIOTHECAIRE)
      on delete restrict on update restrict;

alter table EVENEMENT_BIBLIOTHECAIRE
   add constraint FK_EVENEMEN_ORGANISE2_EVENEMEN foreign key (ID_EVENEMENT)
      references EVENEMENT (ID_EVENEMENT)
      on delete restrict on update restrict;

alter table LIVRE
   add constraint FK_LIVRE_HERITAGE__DOCUMENT foreign key (ID_DOCUMENT)
      references DOCUMENT (ID_DOCUMENT)
      on delete restrict on update restrict;

alter table MEDIA
   add constraint FK_MEDIA_HERITAGE__DOCUMENT foreign key (ID_DOCUMENT)
      references DOCUMENT (ID_DOCUMENT)
      on delete restrict on update restrict;

