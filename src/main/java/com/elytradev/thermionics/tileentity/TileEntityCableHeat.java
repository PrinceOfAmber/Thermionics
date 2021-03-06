/*
 * MIT License
 *
 * Copyright (c) 2017 Isaac Ellingson (Falkreon) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elytradev.thermionics.tileentity;

import com.elytradev.thermionics.api.impl.HeatStorage;
import com.elytradev.thermionics.api.impl.HeatStorageView;
import com.elytradev.thermionics.transport.HeatTransport;
import com.elytradev.thermionics.Thermionics;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityCableHeat extends TileEntityMachine  implements ITickable {
	private HeatStorage heatStorage;
	
	public TileEntityCableHeat() {
		heatStorage = new HeatStorage(200);
		
		heatStorage.listen(this::markDirty);
		capabilities.registerForAllSides(Thermionics.CAPABILITY_HEATSTORAGE, ()->HeatStorageView.of(heatStorage));
	}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setTag("heatStorage", Thermionics.CAPABILITY_HEATSTORAGE.getStorage().writeNBT(Thermionics.CAPABILITY_HEATSTORAGE, heatStorage, null));
		
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		if (tag.hasKey("heatStorage")) {
			NBTBase heatTag = tag.getTag("heatStorage");
			Thermionics.CAPABILITY_HEATSTORAGE.getStorage().readNBT(Thermionics.CAPABILITY_HEATSTORAGE, heatStorage, null, heatTag);
		}
	}

	@Override
	public void update() {
		HeatTransport.diffuse(world, pos, heatStorage);
	}
}
