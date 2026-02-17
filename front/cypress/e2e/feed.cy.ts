describe('Feed', () => {
  beforeEach(() => {
    cy.login('alice@example.com', 'Password1!');
  });

  it('should display posts from subscribed topics', () => {
    cy.visit('/feed');

    cy.get('.card').should('have.length.at.least', 1);
    cy.get('.card__title').should('exist');
  });

  it('should navigate to post detail when clicking a card', () => {
    cy.visit('/feed');

    cy.get('.card').first().click();

    cy.url().should('match', /\/posts\/\d+/);
    cy.get('.page-title--section').should('exist');
  });
});
