-- 矢本クリーンセンター向け 料金区分・アスベスト対応
-- 2026-10-15
--
-- 方針:
--   * 既存の通常単価 unit_value は残す。
--   * 新規 / 既存 / その他の単価を意味が分かるカラム名で追加する。
--   * アスベスト含有時も新規 / 既存 / その他の3単価を持つ。
--   * 現行 unit_value_a / b / c は既存コード互換のため当面残し、
--     初回移行時に new / existing / other へ値をコピーする。
--   * 計量時の料金区分とアスベスト有無を実績として保持できるよう、
--     gate_in_data / weigh_data に price_category / asbestos_flag を追加する。
--
-- price_category:
--   0 = 通常
--   1 = 新規
--   2 = 既存
--   3 = その他

BEGIN;

ALTER TABLE public.m_goods_kind
    ADD COLUMN IF NOT EXISTS unit_value_new integer NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS unit_value_existing integer NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS unit_value_other integer NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS asbestos_unit_value_new integer NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS asbestos_unit_value_existing integer NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS asbestos_unit_value_other integer NOT NULL DEFAULT 0;

COMMENT ON COLUMN public.m_goods_kind.unit_value IS '通常単価（既存仕様互換）';
COMMENT ON COLUMN public.m_goods_kind.unit_value_new IS '新規顧客単価';
COMMENT ON COLUMN public.m_goods_kind.unit_value_existing IS '既存顧客単価';
COMMENT ON COLUMN public.m_goods_kind.unit_value_other IS 'その他顧客単価';
COMMENT ON COLUMN public.m_goods_kind.asbestos_unit_value_new IS 'アスベスト含有・新規顧客単価';
COMMENT ON COLUMN public.m_goods_kind.asbestos_unit_value_existing IS 'アスベスト含有・既存顧客単価';
COMMENT ON COLUMN public.m_goods_kind.asbestos_unit_value_other IS 'アスベスト含有・その他顧客単価';

-- 現行A/B/Cの値を新名称へ初回移行する。
-- すでに新カラム側に値が設定されている場合は上書きしない。
UPDATE public.m_goods_kind
SET unit_value_new = CASE
        WHEN unit_value_new = 0 THEN COALESCE(unit_value_a, 0)
        ELSE unit_value_new
    END,
    unit_value_existing = CASE
        WHEN unit_value_existing = 0 THEN COALESCE(unit_value_b, 0)
        ELSE unit_value_existing
    END,
    unit_value_other = CASE
        WHEN unit_value_other = 0 THEN COALESCE(unit_value_c, 0)
        ELSE unit_value_other
    END;

COMMENT ON COLUMN public.m_goods_kind.unit_value_a IS '旧カラム: unit_value_newへ移行（互換用）';
COMMENT ON COLUMN public.m_goods_kind.unit_value_b IS '旧カラム: unit_value_existingへ移行（互換用）';
COMMENT ON COLUMN public.m_goods_kind.unit_value_c IS '旧カラム: unit_value_otherへ移行（互換用）';

ALTER TABLE public.gate_in_data
    ADD COLUMN IF NOT EXISTS price_category smallint NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS asbestos_flag boolean NOT NULL DEFAULT false;

ALTER TABLE public.weigh_data
    ADD COLUMN IF NOT EXISTS price_category smallint NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS asbestos_flag boolean NOT NULL DEFAULT false;

COMMENT ON COLUMN public.gate_in_data.price_category IS '料金区分 0:通常 1:新規 2:既存 3:その他';
COMMENT ON COLUMN public.weigh_data.price_category IS '料金区分 0:通常 1:新規 2:既存 3:その他';
COMMENT ON COLUMN public.gate_in_data.asbestos_flag IS 'アスベスト含有フラグ false:無 true:有';
COMMENT ON COLUMN public.weigh_data.asbestos_flag IS 'アスベスト含有フラグ false:無 true:有';

-- 2回計量など、滞留データから計量実績が作られる場合は
-- 料金区分とアスベスト有無を自動的に計量実績へ引き継ぐ。
CREATE OR REPLACE FUNCTION public.copy_price_context_to_weigh_data()
RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE
    v_price_category smallint;
    v_asbestos_flag boolean;
BEGIN
    IF NEW.gate_in_sno IS NOT NULL AND NEW.gate_in_sno <> 0 THEN
        SELECT g.price_category, g.asbestos_flag
          INTO v_price_category, v_asbestos_flag
          FROM public.gate_in_data g
         WHERE g.sno = NEW.gate_in_sno
         LIMIT 1;

        IF FOUND THEN
            NEW.price_category := COALESCE(v_price_category, 0);
            NEW.asbestos_flag := COALESCE(v_asbestos_flag, false);
        END IF;
    END IF;
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_copy_price_context_to_weigh_data ON public.weigh_data;
CREATE TRIGGER trg_copy_price_context_to_weigh_data
BEFORE INSERT ON public.weigh_data
FOR EACH ROW
EXECUTE FUNCTION public.copy_price_context_to_weigh_data();

COMMIT;
