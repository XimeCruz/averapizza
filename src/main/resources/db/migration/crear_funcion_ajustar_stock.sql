-- Crear el tipo ENUM si no existe
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo_movimiento') THEN
            CREATE TYPE tipo_movimiento AS ENUM ('ENTRADA', 'SALIDA', 'AJUSTE');
        END IF;
    END $$;

-- Eliminar función si existe (para recrearla)
DROP FUNCTION IF EXISTS fn_ajustar_stock(BIGINT, DOUBLE PRECISION, tipo_movimiento, VARCHAR, INTEGER);

-- Crear la función
CREATE FUNCTION fn_ajustar_stock(
    p_insumo_id BIGINT,
    p_cantidad DOUBLE PRECISION,
    p_tipo_mov tipo_movimiento,
    p_referencia VARCHAR,
    p_usuario_id INTEGER
)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
    v_stock_actual DOUBLE PRECISION;
    v_nuevo_stock DOUBLE PRECISION;
    v_mov_id INTEGER;
    v_nombre_insumo VARCHAR;
BEGIN
    -- Obtener stock actual y nombre del insumo
    SELECT stock_actual, nombre INTO v_stock_actual, v_nombre_insumo
    FROM insumo
    WHERE id = p_insumo_id AND activo = true;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Insumo con ID % no encontrado o está inactivo', p_insumo_id;
    END IF;

    -- Calcular nuevo stock según tipo de movimiento
    CASE p_tipo_mov
        WHEN 'ENTRADA' THEN
            v_nuevo_stock := v_stock_actual + p_cantidad;

        WHEN 'SALIDA' THEN
            IF v_stock_actual < p_cantidad THEN
                RAISE EXCEPTION 'Stock insuficiente del insumo "%". Actual: %, Solicitado: %',
                    v_nombre_insumo, v_stock_actual, p_cantidad;
            END IF;
            v_nuevo_stock := v_stock_actual - p_cantidad;

        WHEN 'AJUSTE' THEN
            v_nuevo_stock := p_cantidad;

        ELSE
            RAISE EXCEPTION 'Tipo de movimiento no válido: %', p_tipo_mov;
        END CASE;

    -- Actualizar stock
    UPDATE insumo
    SET stock_actual = v_nuevo_stock
    WHERE id = p_insumo_id;

    -- Registrar movimiento en inventario
    INSERT INTO mov_inventario (
        tipo_movimiento,
        cantidad,
        fecha_hora,
        referencia,
        insumo_id,
        usuario_id
    ) VALUES (
                 p_tipo_mov::VARCHAR,
                 p_cantidad,
                 NOW(),
                 COALESCE(p_referencia, 'Sin referencia'),
                 p_insumo_id,
                 p_usuario_id
             ) RETURNING id INTO v_mov_id;

    RETURN v_mov_id;
END;
$$;