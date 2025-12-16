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

describe('Template e2e test', () => {
  const templatePageUrl = '/template';
  const templatePageUrlPattern = new RegExp('/template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const templateSample = { nombre: 'ferociously', contenidoMarkdown: 'waist' };

  let template;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/templates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (template) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/templates/${template.id}`,
      }).then(() => {
        template = undefined;
      });
    }
  });

  it('Templates menu should load Templates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('template');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Template').should('exist');
    cy.url().should('match', templatePageUrlPattern);
  });

  describe('Template page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(templatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Template page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/template/new$'));
        cy.getEntityCreateUpdateHeading('Template');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', templatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/templates',
          body: templateSample,
        }).then(({ body }) => {
          template = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/templates?page=0&size=20>; rel="last",<http://localhost/api/templates?page=0&size=20>; rel="first"',
              },
              body: [template],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(templatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Template page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('template');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', templatePageUrlPattern);
      });

      it('edit button click should load edit Template page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Template');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', templatePageUrlPattern);
      });

      it('edit button click should load edit Template page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Template');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', templatePageUrlPattern);
      });

      it('last delete button click should delete instance of Template', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('template').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', templatePageUrlPattern);

        template = undefined;
      });
    });
  });

  describe('new Template page', () => {
    beforeEach(() => {
      cy.visit(`${templatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Template');
    });

    it('should create an instance of Template', () => {
      cy.get(`[data-cy="nombre"]`).type('though');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'though');

      cy.get(`[data-cy="contenidoMarkdown"]`).type('overdub smoggy broadly');
      cy.get(`[data-cy="contenidoMarkdown"]`).should('have.value', 'overdub smoggy broadly');

      cy.get(`[data-cy="variables"]`).type('shallow archaeology after');
      cy.get(`[data-cy="variables"]`).should('have.value', 'shallow archaeology after');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        template = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', templatePageUrlPattern);
    });
  });
});
