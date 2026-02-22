describe('Topic subscription', () => {
  beforeEach(() => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:3001/api/auth/login',
      body: { emailOrUsername: 'bob@example.com', password: 'Password1!' },
    }).then((loginResponse) => {
      const token = loginResponse.body.token;
      window.localStorage.setItem('mdd_token', token);
      cy.request({
        method: 'DELETE',
        url: 'http://localhost:3001/api/topics/1/subscribe',
        headers: { Authorization: `Bearer ${token}` },
        failOnStatusCode: false,
      });
    });
  });

  it('should subscribe to a topic and see the button change', () => {
    cy.visit('/topics');

    cy.contains('button', "S'abonner").first().click();

    cy.contains('button', 'Déjà abonné').should('exist');
  });
});
