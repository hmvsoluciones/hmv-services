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

describe('Servicio e2e test', () => {
  const servicioPageUrl = '/servicio';
  const servicioPageUrlPattern = new RegExp('/servicio(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const servicioSample = {"fechaAtencion":"2025-12-16T10:40:49.246Z","informe":"disconnection"};

  let servicio;
  // let cita;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/citas',
      body: {"fecha":"2025-12-16","horaInicio":"19:36:00","duracionMinutos":914,"descripcion":"orderly except","esUrgente":true,"estadoCita":"PROGRAMADA"},
    }).then(({ body }) => {
      cita = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/servicios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/servicios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/servicios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/citas', {
      statusCode: 200,
      body: [cita],
    });

  });
   */

  afterEach(() => {
    if (servicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicios/${servicio.id}`,
      }).then(() => {
        servicio = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('Servicios menu should load Servicios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('servicio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Servicio').should('exist');
    cy.url().should('match', servicioPageUrlPattern);
  });

  describe('Servicio page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(servicioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Servicio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/servicio/new$'));
        cy.getEntityCreateUpdateHeading('Servicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', servicioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/servicios',
          body: {
            ...servicioSample,
            cita: cita,
          },
        }).then(({ body }) => {
          servicio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/servicios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [servicio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(servicioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(servicioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Servicio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('servicio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', servicioPageUrlPattern);
      });

      it('edit button click should load edit Servicio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Servicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', servicioPageUrlPattern);
      });

      it('edit button click should load edit Servicio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Servicio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', servicioPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Servicio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('servicio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', servicioPageUrlPattern);

        servicio = undefined;
      });
    });
  });

  describe('new Servicio page', () => {
    beforeEach(() => {
      cy.visit(`${servicioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Servicio');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Servicio', () => {
      cy.get(`[data-cy="fechaAtencion"]`).type('2025-12-15T23:10');
      cy.get(`[data-cy="fechaAtencion"]`).blur();
      cy.get(`[data-cy="fechaAtencion"]`).should('have.value', '2025-12-15T23:10');

      cy.get(`[data-cy="informe"]`).type('nifty');
      cy.get(`[data-cy="informe"]`).should('have.value', 'nifty');

      cy.get(`[data-cy="precio"]`).type('11722.11');
      cy.get(`[data-cy="precio"]`).should('have.value', '11722.11');

      cy.get(`[data-cy="cita"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        servicio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', servicioPageUrlPattern);
    });
  });
});
