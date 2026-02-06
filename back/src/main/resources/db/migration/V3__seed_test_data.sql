-- Test data for development
-- All passwords are "Password1!" hashed with BCrypt

-- Users / Password1!
INSERT INTO users (id, email, username, password) VALUES
    (1, 'alice@example.com', 'alice', '$2b$10$JCuzuyOBnszj1OMF1nVkh.B8ohmAjpHfzT1kfwqA4V9D66JiSdDny'),
    (2, 'bob@example.com', 'bob', '$2b$10$JCuzuyOBnszj1OMF1nVkh.B8ohmAjpHfzT1kfwqA4V9D66JiSdDny'),
    (3, 'charlie@example.com', 'charlie', '$2b$10$JCuzuyOBnszj1OMF1nVkh.B8ohmAjpHfzT1kfwqA4V9D66JiSdDny');

-- Subscriptions (alice: Java, Angular, Spring Boot | bob: JavaScript, Angular, Python | charlie: Java, DevOps, Securite)
INSERT INTO subscriptions (user_id, topic_id) VALUES
    (1, 1), (1, 4), (1, 5),
    (2, 2), (2, 4), (2, 3),
    (3, 1), (3, 6), (3, 8);

-- Posts
INSERT INTO posts (id, title, content, author_id, topic_id, created_at) VALUES
    (1, 'Bien debuter avec Spring Boot 3',
     'Spring Boot 3 apporte de nombreuses nouveautes, notamment le passage a Jakarta EE et le support natif de GraalVM. Dans cet article, nous allons voir comment configurer un projet from scratch avec les bonnes pratiques actuelles.\n\nLes points cles :\n- Utiliser Java 17+ (21 recommande)\n- Configurer Spring Security avec JWT\n- Mettre en place Flyway pour les migrations\n- Structurer le projet en couches (Controller/Service/Repository)',
     1, 5, '2026-01-15 10:00:00'),

    (2, 'Angular 19 : les standalone components par defaut',
     'Avec Angular 19, les composants sont standalone par defaut. Cela signifie que vous n avez plus besoin de declarer vos composants dans un NgModule.\n\nSi vous migrez depuis une version anterieure, ajoutez standalone: false aux composants declares dans un NgModule.\n\nLes avantages du standalone :\n- Moins de boilerplate\n- Imports explicites et lisibles\n- Meilleur tree-shaking',
     2, 4, '2026-01-20 14:30:00'),

    (3, 'Les nouveautes de Java 21',
     'Java 21 est une version LTS majeure. Voici les fonctionnalites les plus interessantes :\n\n1. Virtual Threads (Project Loom) - threads legers pour la concurrence\n2. Pattern Matching pour switch - plus expressif\n3. Record Patterns - destructuration des records\n4. Sequenced Collections - nouvelles interfaces ordonnees\n\nCes fonctionnalites rendent Java plus moderne et expressif.',
     3, 1, '2026-01-22 09:15:00'),

    (4, 'TypeScript 5.7 : ce qui change',
     'TypeScript 5.7 apporte des ameliorations notables pour les developpeurs Angular :\n\n- Meilleure inference de types\n- Support ameliore des decorateurs\n- Performance de compilation accrue\n- Nouveaux utilitaires de types\n\nPour migrer, mettez a jour votre tsconfig avec moduleResolution: bundler et target: ES2022.',
     2, 2, '2026-01-25 11:00:00'),

    (5, 'Introduction a Docker pour les devs Java',
     'Docker simplifie le deploiement des applications Spring Boot. Voici un Dockerfile optimise :\n\n1. Utilisez une image multi-stage pour reduire la taille\n2. Separare les dependances du code applicatif pour le cache\n3. Utilisez un utilisateur non-root pour la securite\n4. Exposez le bon port et configurez le healthcheck\n\nAvec Spring Boot 3, vous pouvez aussi utiliser les Buildpacks pour generer l image sans Dockerfile.',
     3, 6, '2026-01-28 16:45:00'),

    (6, 'Securiser une API REST avec JWT',
     'La securisation d une API REST passe par plusieurs etapes :\n\n1. Configurer Spring Security avec un filtre JWT\n2. Hasher les mots de passe avec BCrypt\n3. Generer des tokens a la connexion\n4. Valider les tokens a chaque requete\n5. Gerer les erreurs 401/403 proprement\n\nN oubliez pas de configurer CORS pour autoriser le front-end a communiquer avec l API.',
     1, 8, '2026-02-01 08:30:00'),

    (7, 'Python et le machine learning en 2026',
     'Python reste le langage de reference pour le ML. Les bibliotheques incontournables :\n\n- scikit-learn pour le ML classique\n- PyTorch et TensorFlow pour le deep learning\n- pandas et polars pour la manipulation de donnees\n- matplotlib et seaborn pour la visualisation\n\nLe nouvel ecosysteme autour des LLMs (langchain, llamaindex) ouvre de nouvelles perspectives.',
     2, 3, '2026-02-02 13:00:00');

-- Comments
INSERT INTO comments (content, author_id, post_id, created_at) VALUES
    ('Super article ! Spring Boot 3 est vraiment un bon choix pour les nouveaux projets.', 2, 1, '2026-01-15 12:00:00'),
    ('Merci pour les conseils sur Flyway, je vais l adopter pour mon prochain projet.', 3, 1, '2026-01-15 15:30:00'),

    ('Le passage au standalone est un vrai game changer. Moins de NgModules = moins de complexite.', 1, 2, '2026-01-20 16:00:00'),
    ('Attention a bien ajouter standalone: false quand on migre, sinon ca casse tout !', 3, 2, '2026-01-21 09:00:00'),

    ('Les Virtual Threads sont impressionnants en termes de performance.', 1, 3, '2026-01-22 11:00:00'),
    ('Est-ce que les Virtual Threads remplacent completement les CompletableFuture ?', 2, 3, '2026-01-22 14:00:00'),
    ('Pas completement, mais pour les I/O bound tasks c est bien plus simple.', 3, 3, '2026-01-23 08:00:00'),

    ('moduleResolution bundler m a pose des soucis au debut, mais ca fonctionne bien une fois configure.', 1, 4, '2026-01-25 14:00:00'),

    ('Les Buildpacks Spring Boot sont top, plus besoin de Dockerfile !', 1, 5, '2026-01-29 10:00:00'),
    ('Je prefere quand meme garder le controle avec un Dockerfile custom.', 2, 5, '2026-01-29 11:30:00'),

    ('La gestion des erreurs 401/403 est souvent negligee, merci d en parler.', 3, 6, '2026-02-01 10:00:00'),
    ('N oubliez pas aussi le rate limiting pour eviter le brute force sur le login.', 2, 6, '2026-02-01 12:00:00'),

    ('Polars est vraiment plus rapide que pandas pour les gros datasets.', 1, 7, '2026-02-02 15:00:00');
