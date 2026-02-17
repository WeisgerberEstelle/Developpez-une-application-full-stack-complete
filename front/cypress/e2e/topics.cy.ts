describe('Topic subscription', () => {
  beforeEach(() => {
    cy.login('bob@example.com', 'Password1!');
  });

  it('should subscribe to a topic and see the button change', () => {
    cy.visit('/topics');

    cy.contains('button', "S'abonner").first().click();

    cy.contains('button', 'Déjà abonné').should('exist');
  });
});
