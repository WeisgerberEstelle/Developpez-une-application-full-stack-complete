describe('Create post', () => {
  beforeEach(() => {
    cy.login('alice@example.com', 'Password1!');
  });

  it('should create a post and redirect to the post detail', () => {
    cy.visit('/posts/create');

    cy.get('#topicId').select(1);
    cy.get('#title').type('Cypress E2E Test Post');
    cy.get('#content').type('This post was created during an E2E test.');
    cy.contains('button', 'Créer').click();

    cy.url().should('match', /\/posts\/\d+/);
    cy.contains('Cypress E2E Test Post').should('be.visible');
  });
});

describe('Post detail & comment', () => {
  beforeEach(() => {
    cy.login('alice@example.com', 'Password1!');
  });

  it('should display a post and allow adding a comment', () => {
    cy.visit('/posts/1');

    cy.get('.page-title--section').should('exist');
    cy.get('.post-content').should('exist');

    const commentText = `E2E comment ${Date.now()}`;
    cy.get('textarea[name="newComment"]').type(commentText);
    cy.get('.send-btn').click();

    cy.contains(commentText).should('be.visible');
  });
});
