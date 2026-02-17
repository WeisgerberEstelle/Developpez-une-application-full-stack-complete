declare namespace Cypress {
  interface Chainable {
    login(emailOrUsername: string, password: string): Chainable<void>;
  }
}

Cypress.Commands.add('login', (emailOrUsername: string, password: string) => {
  cy.request({
    method: 'POST',
    url: 'http://localhost:3001/api/auth/login',
    body: { emailOrUsername, password },
  }).then((response) => {
    window.localStorage.setItem('mdd_token', response.body.token);
  });
});
