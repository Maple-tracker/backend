INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (1, '유니크', 15, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (2, '유니크', 21, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (3, '레전드리', 21, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (4, '레전드리', 24, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (5, '레전드리', 27, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (6, '레전드리', 30, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (7, '레전드리', 33, false);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (8, '레전드리', 33, true);
INSERT INTO potential_option (id, grade, stat_percent, potential_ital) VALUES (9, '레전드리', 36, false);

INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (1, '없음', 2, 0);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (2, '에픽', 2, 0);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (3, '유니크', 2, 0);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (4, '유니크', 3, 0);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (5, '레전드리', 2, 0);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (6, '레전드리', 3, 0);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (7, '레전드리', 3, 1);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (8, '레전드리', 3, 2);
INSERT INTO additional_potential_option (id, grade, lines, percent_lines) VALUES (9, '레전드리', 3, 3);

INSERT INTO item_option (item_name, item_slot, star_force, stat_type, potential_option_id, additional_potential_option_id, starforce_scroll_flag, enchanted_flag)
WITH star_forces AS (
    SELECT * FROM (VALUES (17), (18), (19), (22)) AS sf(star_force)
),
stat_types AS (
    SELECT * FROM (VALUES ('STR'), ('DEX'), ('INT'), ('LUK'), ('HP')) AS st(stat_type)
),
potential_ids AS (
    SELECT id FROM potential_option WHERE id BETWEEN 1 AND 9
),
additional_ids AS (
    SELECT id FROM additional_potential_option WHERE id BETWEEN 1 AND 9
),
combinations AS (
    SELECT 
        'PENDANT_ECC1' AS item_name,
        '펜던트' AS item_slot,
        sf.star_force,
        st.stat_type,
        p.id AS potential_option_id,
        a.id AS additional_potential_option_id,
        false AS starforce_scroll_flag,
        true AS enchanted_flag
    FROM star_forces sf
    CROSS JOIN stat_types st
    CROSS JOIN potential_ids p
    CROSS JOIN additional_ids a
)
SELECT * FROM combinations;;
