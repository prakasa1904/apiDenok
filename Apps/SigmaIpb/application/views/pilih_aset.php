<td> <select name="aset" id="aset"><option value="0"> -- Ruangan -- </option>
		
        <?php
		foreach($aset->result() as $r)
		{?>
            <option value="<?php echo $r->kode_lokasi?>"><?php echo $r->nama_ruang?></option>
		<?php }
		?></select>	
		</td>