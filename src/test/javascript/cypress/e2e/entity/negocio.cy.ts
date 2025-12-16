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

describe('Negocio e2e test', () => {
  const negocioPageUrl = '/negocio';
  const negocioPageUrlPattern = new RegExp('/negocio(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const negocioSample = { nombre: 'glow outlaw voluntarily', responsable: 'recompense on', subscriptionKey: 'enormously independence' };

  let negocio;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/negocios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/negocios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/negocios/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (negocio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/negocios/${negocio.id}`,
      }).then(() => {
        negocio = undefined;
      });
    }
  });

  it('Negocios menu should load Negocios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('negocio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Negocio').should('exist');
    cy.url().should('match', negocioPageUrlPattern);
  });

  describe('Negocio page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(negocioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Negocio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/negocio/new$'));
        cy.getEntityCreateUpdateHeading('Negocio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', negocioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/negocios',
          body: negocioSample,
        }).then(({ body }) => {
          negocio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/negocios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/negocios?page=0&size=20>; rel="last",<http://localhost/api/negocios?page=0&size=20>; rel="first"',
              },
              body: [negocio],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(negocioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Negocio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('negocio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', negocioPageUrlPattern);
      });

      it('edit button click should load edit Negocio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Negocio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', negocioPageUrlPattern);
      });

      it('edit button click should load edit Negocio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Negocio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', negocioPageUrlPattern);
      });

      it('last delete button click should delete instance of Negocio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('negocio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', negocioPageUrlPattern);

        negocio = undefined;
      });
    });
  });

  describe('new Negocio page', () => {
    beforeEach(() => {
      cy.visit(`${negocioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Negocio');
    });

    it('should create an instance of Negocio', () => {
      cy.get(`[data-cy="nombre"]`).type('opposite');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'opposite');

      cy.get(`[data-cy="responsable"]`).type('boohoo');
      cy.get(`[data-cy="responsable"]`).should('have.value', 'boohoo');

      cy.get(`[data-cy="celular"]`).type('fantastic oof graduate');
      cy.get(`[data-cy="celular"]`).should('have.value', 'fantastic oof graduate');

      cy.get(`[data-cy="correo"]`).type('versus');
      cy.get(`[data-cy="correo"]`).should('have.value', 'versus');

      cy.get(`[data-cy="subscriptionKey"]`).type('please seldom');
      cy.get(`[data-cy="subscriptionKey"]`).should('have.value', 'please seldom');

      cy.get(`[data-cy="esActivo"]`).should('not.be.checked');
      cy.get(`[data-cy="esActivo"]`).click();
      cy.get(`[data-cy="esActivo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        negocio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', negocioPageUrlPattern);
    });
  });
});
