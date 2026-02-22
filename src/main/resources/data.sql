INSERT INTO coupon (
  id,
  code,
  description,
  discount_value,
  expiration_date,
  deleted_at,
  status,
  published,
  redeemed
) VALUES
(
  'cef9d1e3-aae5-4ab6-a297-358c6032b1e7',
  'ABC123',
  'Cupom de desconto de 10%',
  10.0,
  '2026-12-20T00:00:00Z',
  NULL,
  'ACTIVE',
  FALSE,
  FALSE
),
(
  '7a1c6f2a-9c7a-4c2b-ae74-6f0c9b9c2e11',
  'DEF456',
  'Cupom de desconto de 30%',
  30.0,
  '2026-11-28T00:00:00Z',
  NULL,
  'ACTIVE',
  FALSE,
  FALSE
);