describe('Register', () => {
  it('should register a new user and redirect to /topics', () => {
    const unique = Date.now();
    cy.visit('/register');

    cy.get('#username').type(`user${unique}`);
    cy.get('#email').type(`user${unique}@test.com`);
    cy.get('#password').type('Password1!');
    cy.contains('button', "S'inscrire").click();

    cy.url().should('include', '/topics');
  });
});

describe('Login', () => {
  it('should login with valid email and redirect to /feed', () => {
    cy.visit('/login');

    cy.get('#emailOrUsername').type('alice@example.com');
    cy.get('#password').type('Password1!');
    cy.contains('button', 'Se connecter').click();

    cy.url().should('include', '/feed');
  });

  it('should display error message with invalid credentials', () => {
    cy.visit('/login');

    cy.get('#emailOrUsername').type('alice@example.com');
    cy.get('#password').type('WrongPassword1!');
    cy.contains('button', 'Se connecter').click();

    cy.get('.error-message').should('be.visible');
  });
});

describe('Auth guard', () => {
  it('should redirect to home when accessing /feed without being logged in', () => {
    cy.visit('/feed');

    cy.url().should('eq', Cypress.config().baseUrl + '/');
  });
});
