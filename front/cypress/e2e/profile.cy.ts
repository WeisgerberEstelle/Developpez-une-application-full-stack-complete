describe('Profile update', () => {
  beforeEach(() => {
    cy.login('bob@example.com', 'Password1!');
  });

  it('should update username and show success message', () => {
    cy.visit('/profile');

    const newUsername = `bob-e2e-${Date.now()}`;
    cy.get('#username').clear().type(newUsername);
    cy.contains('button', 'Sauvegarder').click();

    cy.get('.success').should('contain', 'Profile updated successfully');

    cy.get('#username').clear().type('bob');
    cy.contains('button', 'Sauvegarder').click();
    cy.get('.success').should('be.visible');
  });
});

describe('Logout', () => {
  beforeEach(() => {
    cy.login('bob@example.com', 'Password1!');
  });

  it('should logout and redirect to home page', () => {
    cy.visit('/profile');

    cy.contains('Se déconnecter').click();

    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.contains('button', 'Se connecter').should('be.visible');
  });
});

describe('Unsubscribe from profile', () => {
  beforeEach(() => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:3001/api/auth/login',
      body: { emailOrUsername: 'charlie@example.com', password: 'Password1!' },
    }).then((loginResponse) => {
      const token = loginResponse.body.token;
      window.localStorage.setItem('mdd_token', token);
      cy.request({
        method: 'POST',
        url: 'http://localhost:3001/api/topics/1/subscribe',
        headers: { Authorization: `Bearer ${token}` },
        failOnStatusCode: false,
      });
    });
  });

  it('should unsubscribe from a topic on the profile page', () => {
    cy.visit('/profile');

    cy.get('.card').then(($cards) => {
      const countBefore = $cards.length;

      cy.get('.card').first().contains('button', 'Se désabonner').click();

      cy.get('.card').should('have.length', countBefore - 1);
    });
  });
});
