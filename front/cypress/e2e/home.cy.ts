describe('Home page', () => {
  it('should display login and register buttons', () => {
    cy.visit('/');

    cy.contains('button', 'Se connecter').should('be.visible');
    cy.contains('button', "S'inscrire").should('be.visible');
  });
});
