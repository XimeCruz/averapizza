package com.xime.averapizza.config;

import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            SaborPizzaRepository saborPizzaRepository,
            PresentacionProductoRepository presentacionRepository,
            InsumoRepository insumoRepository,
            RecetaRepository recetaRepository,
            RecetaDetalleRepository recetaDetalleRepository,
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            PrecioSaborPresentacionRepository precioSaborPresentacionRepository
    ) {
        return args -> {
            if (productoRepository.count() > 0) {
                System.out.println("====================================");
                System.out.println("⚠️ DATOS YA EXISTEN - Omitiendo carga");
                System.out.println("====================================");
                return;
            }
            // ============ 1. CREAR ROLES ============
            Rol rolAdmin = crearRolSiNoExiste(rolRepository, Rol.RolNombre.ADMIN);
            Rol rolCajero = crearRolSiNoExiste(rolRepository, Rol.RolNombre.CAJERO);
            Rol rolCliente = crearRolSiNoExiste(rolRepository, Rol.RolNombre.CLIENTE);
            Rol rolCocina = crearRolSiNoExiste(rolRepository, Rol.RolNombre.COCINA);

            // ============ 2. CREAR USUARIOS ============
            Usuario admin = crearUsuario(usuarioRepository, "Admin Principal", "admin@averapizza.com",
                    "admin123", true, Set.of(rolAdmin));

            Usuario cajero1 = crearUsuario(usuarioRepository, "María González", "maria@averapizza.com",
                    "cajero123", true, Set.of(rolCajero));

            Usuario cocinero1 = crearUsuario(usuarioRepository, "Carlos Pérez", "carlos@averapizza.com",
                    "cocina123", true, Set.of(rolCocina));

            // Crear 10 clientes
            List<Usuario> clientes = new ArrayList<>();
            String[] nombresClientes = {
                    "Juan Martínez", "Ana López", "Pedro Sánchez", "Laura García",
                    "Diego Torres", "Sofía Ramírez", "Miguel Ángel Ruiz", "Carmen Flores",
                    "Roberto Jiménez", "Isabel Morales"
            };

            for (int i = 0; i < nombresClientes.length; i++) {
                boolean activo = i < 8; // Los primeros 8 activos, últimos 2 inactivos
                Usuario cliente = crearUsuario(usuarioRepository,
                        nombresClientes[i],
                        "cliente" + (i + 1) + "@gmail.com",
                        "cliente123",
                        activo,
                        Set.of(rolCliente));
                clientes.add(cliente);
            }

            // ============ 3. CREAR PRODUCTOS ============
            Producto productoPizza = crearProducto(productoRepository, "Pizza",
                    Producto.TipoProducto.PIZZA, true, true);

            Producto productoBebida = crearProducto(productoRepository, "Bebida Carbonatada",
                    Producto.TipoProducto.BEBIDA, true, true);

            // ============ 4. CREAR PRESENTACIONES ============
            PresentacionProducto presPeso = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.PESO, true, 2,
                    productoPizza.getId().intValue(), 15.0, true);

            PresentacionProducto presRedonda = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.REDONDA, false, 1,
                    productoPizza.getId().intValue(), 80.0, true);

            PresentacionProducto presBandeja = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.BANDEJA, false, 3,
                    productoPizza.getId().intValue(), 120.0, true);

            PresentacionProducto presBebida330 = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.ML_330, false, 0,
                    productoBebida.getId().intValue(), 0.0, true);

            PresentacionProducto presBebida750 = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.ML_750, false, 0,
                    productoBebida.getId().intValue(), 0.0, true);

            PresentacionProducto presBebida2L = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.L_2, false, 0,
                    productoBebida.getId().intValue(), 0.0, true);

            PresentacionProducto presBebida3L = crearPresentacion(presentacionRepository,
                    PresentacionProducto.TipoPresentacion.L_3, false, 0,
                    productoBebida.getId().intValue(), 0.0, true);

            // ============ 5. CREAR SABORES DE PIZZA ============
            List<SaborPizza> sabores = new ArrayList<>();
            String[] nombresSabores = {
                    "Hawaiana", "Pepperoni", "Vegetariana", "Carnes Frías",
                    "Mexicana", "BBQ", "Cuatro Quesos", "Margarita"
            };

            for (String nombreSabor : nombresSabores) {
                SaborPizza sabor = crearSabor(saborPizzaRepository, nombreSabor,
                        "Deliciosa pizza " + nombreSabor.toLowerCase(), productoPizza, true);
                sabores.add(sabor);

                // Crear precios por presentación para cada sabor
                crearPrecioSabor(precioSaborPresentacionRepository, sabor, presPeso, 18.0);
                crearPrecioSabor(precioSaborPresentacionRepository, sabor, presRedonda, 85.0);
                crearPrecioSabor(precioSaborPresentacionRepository, sabor, presBandeja, 130.0);
            }

            List<SaborPizza> saboresBebida = new ArrayList<>();
            String[] nombresBebidas = {"Coca Cola", "Sprite", "Fanta", "Inca Kola"};
            double[][] preciosBebidas = {
                    {5.0, 12.0, 20.0, 28.0},  // Coca Cola
                    {5.0, 12.0, 20.0, 28.0},  // Sprite
                    {4.5, 11.0, 18.0, 25.0},  // Fanta
                    {5.5, 13.0, 22.0, 30.0}   // Inca Kola
            };

            for (int i = 0; i < nombresBebidas.length; i++) {
                SaborPizza saborBebida = crearSabor(saborPizzaRepository, nombresBebidas[i],
                        "Bebida refrescante " + nombresBebidas[i], productoBebida, true);
                saboresBebida.add(saborBebida);

                // Crear precios por presentación
                crearPrecioSabor(precioSaborPresentacionRepository, saborBebida, presBebida330, preciosBebidas[i][0]);
                crearPrecioSabor(precioSaborPresentacionRepository, saborBebida, presBebida750, preciosBebidas[i][1]);
                crearPrecioSabor(precioSaborPresentacionRepository, saborBebida, presBebida2L, preciosBebidas[i][2]);
                crearPrecioSabor(precioSaborPresentacionRepository, saborBebida, presBebida3L, preciosBebidas[i][3]);
            }


            // ============ 6. CREAR INSUMOS ============
            List<Insumo> insumos = new ArrayList<>();
            insumos.add(crearInsumo(insumoRepository, "Harina", "kg", 50.0, 10.0, true));
            insumos.add(crearInsumo(insumoRepository, "Queso Mozzarella", "kg", 15.0, 5.0, true)); // Bajo stock
            insumos.add(crearInsumo(insumoRepository, "Salsa de Tomate", "litros", 20.0, 8.0, true));
            insumos.add(crearInsumo(insumoRepository, "Pepperoni", "kg", 8.0, 3.0, true)); // Crítico
            insumos.add(crearInsumo(insumoRepository, "Jamón", "kg", 12.0, 5.0, true));
            insumos.add(crearInsumo(insumoRepository, "Piña", "kg", 5.0, 2.0, true)); // Crítico
            insumos.add(crearInsumo(insumoRepository, "Champiñones", "kg", 25.0, 8.0, true));
            insumos.add(crearInsumo(insumoRepository, "Pimientos", "kg", 18.0, 6.0, true));

            // ============ 7. CREAR RECETAS ============
            for (SaborPizza sabor : sabores) {
                Receta receta = crearReceta(recetaRepository, sabor, true);

                // Ingredientes básicos para todas las pizzas
                crearRecetaDetalle(recetaDetalleRepository, receta, insumos.get(0), 0.3); // Harina
                crearRecetaDetalle(recetaDetalleRepository, receta, insumos.get(1), 0.2); // Queso
                crearRecetaDetalle(recetaDetalleRepository, receta, insumos.get(2), 0.1); // Salsa

                // Ingredientes específicos por sabor
                if (sabor.getNombre().equals("Hawaiana")) {
                    crearRecetaDetalle(recetaDetalleRepository, receta, insumos.get(4), 0.15); // Jamón
                    crearRecetaDetalle(recetaDetalleRepository, receta, insumos.get(5), 0.15); // Piña
                } else if (sabor.getNombre().equals("Pepperoni")) {
                    crearRecetaDetalle(recetaDetalleRepository, receta, insumos.get(3), 0.2); // Pepperoni
                }
            }

            // ============ 8. CREAR PEDIDOS Y VENTAS ============
            Random random = new Random();
            LocalDateTime now = LocalDateTime.now();

            // Crear pedidos de los últimos 30 días
            for (int dia = 30; dia >= 0; dia--) {
                LocalDateTime fechaPedido = now.minusDays(dia);
                int pedidosDelDia = random.nextInt(5) + 3; // 3-7 pedidos por día

                for (int p = 0; p < pedidosDelDia; p++) {
                    Usuario cliente = clientes.get(random.nextInt(clientes.size()));

                    // Determinar estado del pedido
                    EstadoPedido estado;
                    if (dia == 0) { // Hoy
                        estado = new EstadoPedido[]{
                                EstadoPedido.PENDIENTE,
                                EstadoPedido.EN_PREPARACION,
                                EstadoPedido.LISTO
                        }[random.nextInt(3)];
                    } else { // Días anteriores
                        estado = random.nextDouble() < 0.9 ? EstadoPedido.ENTREGADO : EstadoPedido.CANCELADO;
                    }

                    TipoServicio tipoServicio = TipoServicio.values()[random.nextInt(TipoServicio.values().length)];

                    Pedido pedido = new Pedido();
                    pedido.setEstado(estado);
                    pedido.setTipoServicio(tipoServicio);
                    pedido.setFechaHora(fechaPedido.plusHours(random.nextInt(12) + 8)); // Entre 8am y 8pm
                    pedido.setUsuarioId(cliente.getId().intValue());
                    pedido.setDetalles(new ArrayList<>());

                    double totalPedido = 0.0;
                    int numProductos = random.nextInt(3) + 1; // 1-3 productos

                    for (int i = 0; i < numProductos; i++) {
                        DetallePedido detalle = new DetallePedido();
                        detalle.setPedido(pedido);
                        detalle.setProducto(productoPizza);
                        detalle.setCantidad(1);

                        // Elegir presentación aleatoria
                        PresentacionProducto pres = new PresentacionProducto[]{
                                presPeso, presRedonda, presBandeja
                        }[random.nextInt(3)];
                        detalle.setPresentacion(pres);

                        // Asignar sabores según presentación
                        SaborPizza sabor1 = sabores.get(random.nextInt(sabores.size()));
                        detalle.setSabor1(sabor1);
                        detalle.setPrecioUnitario(pres.getPrecioBase());

                        if (pres.getMaxSabores() >= 2 && random.nextBoolean()) {
                            detalle.setSabor2(sabores.get(random.nextInt(sabores.size())));
                        }

                        if (pres.getMaxSabores() == 3 && random.nextBoolean()) {
                            detalle.setSabor3(sabores.get(random.nextInt(sabores.size())));
                        }

                        if (pres.getUsaPeso()) {
                            double peso = 0.5 + random.nextDouble() * 1.5; // 0.5 a 2.0 kg
                            detalle.setPesoKg(peso);
                            detalle.setSubtotal(peso * pres.getPrecioBase());
                        } else {
                            detalle.setSubtotal(pres.getPrecioBase());
                        }

                        totalPedido += detalle.getSubtotal();
                        pedido.getDetalles().add(detalle);
                    }

                    pedido.setTotal(totalPedido);
                    pedido = pedidoRepository.save(pedido);

                    // Crear venta si el pedido está entregado
                    if (estado == EstadoPedido.ENTREGADO) {
                        Venta venta = new Venta();
                        venta.setFecha(pedido.getFechaHora());
                        venta.setTotal(pedido.getTotal());
                        venta.setPedido(pedido);
                        venta.setDetalles(new ArrayList<>());

                        for (DetallePedido dp : pedido.getDetalles()) {
                            DetalleVenta dv = new DetalleVenta();
                            dv.setVenta(venta);
                            dv.setProducto(dp.getProducto());
                            dv.setCantidad(dp.getCantidad());
                            dv.setSubtotal(dp.getSubtotal());
                            venta.getDetalles().add(dv);
                        }

                        ventaRepository.save(venta);
                    }
                }
            }

            System.out.println("====================================");
            System.out.println("DATOS DE PRUEBA CARGADOS");
            System.out.println("====================================");
            System.out.println("Usuarios creados:");
            System.out.println("   - Admin: admin@averapizza.com / admin123");
            System.out.println("   - Cajero: maria@averapizza.com / cajero123");
            System.out.println("   - Cocina: carlos@averapizza.com / cocina123");
            System.out.println("   - 10 Clientes: cliente1@gmail.com ... cliente10@gmail.com / cliente123");
            System.out.println("Sabores de pizza: " + sabores.size());
            System.out.println("Insumos: " + insumos.size());
            System.out.println("Pedidos generados: ~" + (30 * 5));
            System.out.println("Ventas registradas");
            System.out.println("Alertas de stock: Queso, Pepperoni y Piña en nivel bajo/crítico");
            System.out.println("====================================");
        };
    }

    // ============ MÉTODOS AUXILIARES ============

    private Rol crearRolSiNoExiste(RolRepository repository, Rol.RolNombre nombre) {
        return repository.findByNombre(nombre)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setNombre(nombre);
                    return repository.save(rol);
                });
    }

    private Usuario crearUsuario(UsuarioRepository repository, String nombre, String correo,
                                 String password, boolean activo, Set<Rol> roles) {

        Usuario usuarioExistente = repository.findByCorreo(correo);
        if (usuarioExistente != null) {
            return usuarioExistente;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setActivo(activo);
        usuario.setRoles(roles);
        return repository.save(usuario);
    }

    private Producto crearProducto(ProductoRepository repository, String nombre,
                                   Producto.TipoProducto tipo, boolean tieneSabores, boolean activo) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setTipoProducto(tipo);
        producto.setTieneSabores(tieneSabores);
        producto.setActivo(activo);
        return repository.save(producto);
    }

    private PresentacionProducto crearPresentacion(PresentacionProductoRepository repository,
                                                   PresentacionProducto.TipoPresentacion tipo,
                                                   boolean usaPeso, int maxSabores, int productoId,
                                                   double precioBase, boolean activo) {
        PresentacionProducto pres = new PresentacionProducto();
        pres.setTipo(tipo);
        pres.setUsaPeso(usaPeso);
        pres.setMaxSabores(maxSabores);
        pres.setProductoId(productoId);
        pres.setPrecioBase(precioBase);
        pres.setActivo(activo);
        return repository.save(pres);
    }

    private SaborPizza crearSabor(SaborPizzaRepository repository, String nombre,
                                  String descripcion, Producto producto, boolean activo) {
        SaborPizza sabor = new SaborPizza();
        sabor.setNombre(nombre);
        sabor.setDescripcion(descripcion);
        sabor.setProducto(producto);
        sabor.setActivo(activo);
        return repository.save(sabor);
    }

    private void crearPrecioSabor(PrecioSaborPresentacionRepository repository,
                                  SaborPizza sabor, PresentacionProducto presentacion, double precio) {
        PrecioSaborPresentacion precioSabor = new PrecioSaborPresentacion();
        precioSabor.setSabor(sabor);
        precioSabor.setPresentacion(presentacion);
        precioSabor.setPrecio(precio);
        repository.save(precioSabor);
    }

    private Insumo crearInsumo(InsumoRepository repository, String nombre, String unidad,
                               double stockActual, double stockMinimo, boolean activo) {
        Insumo insumo = new Insumo();
        insumo.setNombre(nombre);
        insumo.setUnidadMedida(unidad);
        insumo.setStockActual(stockActual);
        insumo.setStockMinimo(stockMinimo);
        insumo.setActivo(activo);
        return repository.save(insumo);
    }

    private Receta crearReceta(RecetaRepository repository, SaborPizza sabor, boolean activo) {
        Receta receta = new Receta();
        receta.setSabor(sabor);
        receta.setActivo(activo);
        receta.setDetalles(new ArrayList<>());
        return repository.save(receta);
    }

    private void crearRecetaDetalle(RecetaDetalleRepository repository, Receta receta,
                                    Insumo insumo, double cantidad) {
        RecetaDetalle detalle = new RecetaDetalle();
        detalle.setReceta(receta);
        detalle.setInsumo(insumo);
        detalle.setCantidad(cantidad);
        repository.save(detalle);
    }
}
