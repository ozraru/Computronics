package pl.asie.computronics.integration.fsp;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import flaxbeard.steamcraft.api.ISteamTransporter;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import pl.asie.computronics.integration.CCTilePeripheral;
import pl.asie.computronics.integration.ManagedEnvironmentOCTile;
import pl.asie.computronics.reference.Names;

public class DriverSteamTransporter {
	public static class OCDriver extends DriverTileEntity {
		public class InternalManagedEnvironment extends ManagedEnvironmentOCTile<ISteamTransporter> {
			public InternalManagedEnvironment(ISteamTransporter tile) {
				super(tile, Names.FSP_SteamTransporter);
			}

			@Callback(direct = true)
			public Object[] getSteamPressure(Context c, Arguments a) {
				return new Object[] { tile.getPressure() };
			}

			@Callback(direct = true)
			public Object[] getSteamCapacity(Context c, Arguments a) {
				return new Object[] { tile.getCapacity() };
			}

			@Callback(direct = true)
			public Object[] getSteamAmount(Context c, Arguments a) {
				return new Object[] { tile.getSteam() };
			}
		}

		@Override
		public Class<?> getTileEntityClass() {
			return ISteamTransporter.class;
		}

		@Override
		public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
			return new InternalManagedEnvironment((ISteamTransporter) world.getTileEntity(x, y, z));
		}
	}

	public static class CCDriver extends CCTilePeripheral<ISteamTransporter> {

		public CCDriver() {
		}

		public CCDriver(ISteamTransporter block, World world, int x, int y, int z) {
			super(block, Names.FSP_SteamTransporter, world, x, y, z);
		}

		@Override
		public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
			Block block = world.getBlock(x, y, z);
			if(block instanceof ISteamTransporter) {
				return new CCDriver((ISteamTransporter) block, world, x, y, z);
			}
			return null;
		}

		@Override
		public String[] getMethodNames() {
			return new String[] { "getSteamPressure", "getSteamCapacity", "getSteamAmount" };
		}

		@Override
		public Object[] callMethod(IComputerAccess computer, ILuaContext context,
			int method, Object[] arguments) throws LuaException,
			InterruptedException {
			switch(method){
				case 0:
					return new Object[] { (double) tile.getPressure() };
				case 1:
					return new Object[] { tile.getCapacity() };
				case 2:
					return new Object[] { tile.getSteam() };
			}
			return null;
		}
	}
}
