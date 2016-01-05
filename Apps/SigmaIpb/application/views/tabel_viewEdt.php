<?php
if($titik->num_rows() != 0) //$titik->num_rows() counting jumlah data titik berupa array
{
  ?>
   <input type="hidden" id="jumWing" value="<?php echo $wing->num_rows();?>">
  <?php
    $nama = "namawing";
    $ulang = 1;
  	foreach($wing->result() as $w){
	?>
	<input type="hidden" id="<?php echo $nama.$ulang?>" value="<?php echo $w->wing?>">
	<?php
	$ulang++;
	}
  ?>
  <table width="893" border="1" align="center">
     
  <?php $i=1; $in=0;  foreach($titik->result() as $r) //$titik->result() hasil array dari pengolahan query data titik lalu dilooping
  { 
  	if ($r->wing == 1)
	{$color = "#df382a";}
	elseif ( $r->wing ==2)
	{$color = "#942697";}
	elseif ( $r->wing ==3)
	{$color = "#4fe0e8";}
	elseif ( $r->wing ==4)
	{$color = "#f79656";}
	elseif ( $r->wing ==5)
	{$color = "#ebef43";}
	elseif ( $r->wing ==6)
	{$color = "#f780a0";}
	elseif ( $r->wing ==7)
	{$color = "#dc98de";}
	elseif ( $r->wing ==8)
	{$color = "#2b880f";}
	elseif ( $r->wing ==9)
	{$color = "#8f68bf";}
	elseif ( $r->wing ==10)
	{$color = "#f97424";}
	elseif ( $r->wing ==11)
	{$color = "#ff1e1e";}
	elseif ( $r->wing ==12)
	{$color = "#faff24";}
	elseif ( $r->wing ==13)
	{$color = "#03fef8";}
	elseif ( $r->wing ==14)
	{$color = "#311aff";}
	elseif ( $r->wing ==15)
	{$color = "#da17df";}
	elseif ( $r->wing ==16)
	{$color = "#66d623";}
	elseif ( $r->wing ==17)
	{$color = "#f44448";}
	elseif ( $r->wing ==18)
	{$color = "#60a8b0";}
	elseif ( $r->wing ==19)
	{$color = "#39287d";}
	elseif ( $r->wing ==20)
	{$color = "#9f0634";}
	elseif ( $r->wing ==21)
	{$color = "#6792e9";}
	elseif ( $r->wing ==22)
	{$color = "#8c01af";}
	elseif ( $r->wing ==23)
	{$color = "#1edd91";}
	elseif ( $r->wing ==24)
	{$color = "#cece64";}
	else
	{$color = "#ffffff";}

	if($i==1)
	{
		$temp[$in] = $r->wing;
	}
/*	else
	{
		$cek=$r->wing;
		if($temp==$cek)
		{
			
		}
		else {
			
		
			$in++;
			$temp[$in]=$cek;
		}
	}*/
	
	
  ?>
  
  <tr>
  <!--<td style="background-color: <?php echo $color; ?>" width="40"><?php echo $r->wing;?></td>-->
  <td width="20"><?php echo $i++;?></td>
  <td width="250"><?php echo $r->nama_barang;?></td>
  <td width="150"><?php echo $r->merk_type;?></td>
  <td width="74"><?php echo $r->tahun;?></td>
  <td width="235"><?php echo $r->nama_ruang;?></td>
  <td>
<a href="<?=base_url()?>index.php/map/editas/<?php echo $r->id_barang;?>"><font color="#2600ff" onmouseover="this.fontcolor'black'">Ubah</font></a> |
<a href="<?=base_url()?>index.php/map/delete/<?php echo $r->id_barang;?>"  onclick="return confirm('Anda yakin akan menghapus data?')"><font color="#ff0000">Hapus</font></a>
</td>
   </tr>
  <?php } ?>
  
  </table>
  <?php 
/* foreach($temp as $t)
 {
 	echo $temp;
 	
 }*/
//echo $temp[1];

}

else
{
echo 'Tidak Ada Data';
}


?>
