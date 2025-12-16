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

describe('Empleado e2e test', () => {
  const empleadoPageUrl = '/empleado';
  const empleadoPageUrlPattern = new RegExp('/empleado(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const empleadoSample = { nombre: 'warmhearted very nor' };

  let empleado;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/empleados+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/empleados').as('postEntityRequest');
    cy.intercept('DELETE', '/api/empleados/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (empleado) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/empleados/${empleado.id}`,
      }).then(() => {
        empleado = undefined;
      });
    }
  });

  it('Empleados menu should load Empleados page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('empleado');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Empleado').should('exist');
    cy.url().should('match', empleadoPageUrlPattern);
  });

  describe('Empleado page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(empleadoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Empleado page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/empleado/new$'));
        cy.getEntityCreateUpdateHeading('Empleado');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', empleadoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/empleados',
          body: empleadoSample,
        }).then(({ body }) => {
          empleado = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/empleados+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/empleados?page=0&size=20>; rel="last",<http://localhost/api/empleados?page=0&size=20>; rel="first"',
              },
              body: [empleado],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(empleadoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Empleado page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('empleado');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', empleadoPageUrlPattern);
      });

      it('edit button click should load edit Empleado page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Empleado');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', empleadoPageUrlPattern);
      });

      it('edit button click should load edit Empleado page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Empleado');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', empleadoPageUrlPattern);
      });

      it('last delete button click should delete instance of Empleado', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('empleado').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', empleadoPageUrlPattern);

        empleado = undefined;
      });
    });
  });

  describe('new Empleado page', () => {
    beforeEach(() => {
      cy.visit(`${empleadoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Empleado');
    });

    it('should create an instance of Empleado', () => {
      cy.get(`[data-cy="nombre"]`).type('mousse');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'mousse');

      cy.get(`[data-cy="celular"]`).type('like um');
      cy.get(`[data-cy="celular"]`).should('have.value', 'like um');

      cy.get(`[data-cy="especialidad"]`).type('yesterday');
      cy.get(`[data-cy="especialidad"]`).should('have.value', 'yesterday');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        empleado = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', empleadoPageUrlPattern);
    });
  });
});
