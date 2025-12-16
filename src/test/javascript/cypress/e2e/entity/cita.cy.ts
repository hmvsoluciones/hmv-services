import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Cita e2e test', () => {
  const citaPageUrl = '/cita';
  const citaPageUrlPattern = new RegExp('/cita(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const citaSample = { fecha: '2025-12-16', horaInicio: '06:28:00', descripcion: 'basket colonialism ha' };

  let cita;
  let cliente;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/clientes',
      body: {
        nombre: 'around',
        celular: 'why gosh',
        correo: 'by plus before',
        identificacion: 'definitive faint',
        direccion: 'upliftingly zowie',
        activo: false,
      },
    }).then(({ body }) => {
      cliente = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/citas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/citas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/citas/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/clientes', {
      statusCode: 200,
      body: [cliente],
    });

    cy.intercept('GET', '/api/empleados', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (cita) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/citas/${cita.id}`,
      }).then(() => {
        cita = undefined;
      });
    }
  });

  afterEach(() => {
    if (cliente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/clientes/${cliente.id}`,
      }).then(() => {
        cliente = undefined;
      });
    }
  });

  it('Citas menu should load Citas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cita');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cita').should('exist');
    cy.url().should('match', citaPageUrlPattern);
  });

  describe('Cita page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(citaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cita page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cita/new$'));
        cy.getEntityCreateUpdateHeading('Cita');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', citaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/citas',
          body: {
            ...citaSample,
            cliente,
          },
        }).then(({ body }) => {
          cita = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/citas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/citas?page=0&size=20>; rel="last",<http://localhost/api/citas?page=0&size=20>; rel="first"',
              },
              body: [cita],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(citaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cita page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cita');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', citaPageUrlPattern);
      });

      it('edit button click should load edit Cita page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cita');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', citaPageUrlPattern);
      });

      // Reason: jpaMetamodelFiltering with required relationship missbehavior
      it.skip('edit button click should load edit Cita page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cita');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', citaPageUrlPattern);
      });

      it('last delete button click should delete instance of Cita', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cita').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', citaPageUrlPattern);

        cita = undefined;
      });
    });
  });

  describe('new Cita page', () => {
    beforeEach(() => {
      cy.visit(`${citaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cita');
    });

    it('should create an instance of Cita', () => {
      cy.get(`[data-cy="fecha"]`).type('2025-12-16');
      cy.get(`[data-cy="fecha"]`).blur();
      cy.get(`[data-cy="fecha"]`).should('have.value', '2025-12-16');

      cy.get(`[data-cy="horaInicio"]`).type('00:35:00');
      cy.get(`[data-cy="horaInicio"]`).invoke('val').should('match', new RegExp('00:35:00'));

      cy.get(`[data-cy="duracionMinutos"]`).type('754');
      cy.get(`[data-cy="duracionMinutos"]`).should('have.value', '754');

      cy.get(`[data-cy="descripcion"]`).type('versus');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'versus');

      cy.get(`[data-cy="esUrgente"]`).should('not.be.checked');
      cy.get(`[data-cy="esUrgente"]`).click();
      cy.get(`[data-cy="esUrgente"]`).should('be.checked');

      cy.get(`[data-cy="estadoCita"]`).select('PROGRAMADA');

      cy.get(`[data-cy="cliente"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cita = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', citaPageUrlPattern);
    });
  });
});
