-- 矢本クリーンセンター: 請求先マスタの料金区分
-- 2026-10-22
-- 0: 通常 / 1: 新規 / 2: 既存 / 3: その他
-- 既存の請求先は通常(0)に設定する。計量実績や請求データは更新しない。
BEGIN;

ALTER TABLE public.m_bill
    ADD COLUMN IF NOT EXISTS price_category smallint NOT NULL DEFAULT 0;

COMMENT ON COLUMN public.m_bill.price_category
    IS '料金区分 0:通常 1:新規 2:既存 3:その他';

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
          FROM pg_constraint
         WHERE conrelid = 'public.m_bill'::regclass
           AND conname = 'm_bill_price_category_check'
    ) THEN
        ALTER TABLE public.m_bill
            ADD CONSTRAINT m_bill_price_category_check
            CHECK (price_category BETWEEN 0 AND 3);
    END IF;
END
$$;

COMMIT;

-- 確認用（適用後に任意実行）
-- SELECT price_category, count(*)
--   FROM public.m_bill
--  GROUP BY price_category
--  ORDER BY price_category;
