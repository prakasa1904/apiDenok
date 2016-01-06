<style>
.highlight{
	background-color: #FF0;
	padding: 4px;
}
</style>
<h4> Cari Data </h4>
<?php 
$option = array('nim' => 'Nim', 'nama' => 'Nama', 'kota' => 'Kota');
echo $form->create('Search', array('url' => array('action' => 'index'), 'inputDefaults' => array('label' => false, 'div' => false))); ?>
<table>
	<tr>
		<td> <div id="tahoma"> Kategori </div> </td>
		<td> <div id="tahoma"> : </div> </td>
		<td> <?php echo $form->select('field', $option); ?> </td>
	</tr>
	<tr>
		<td> <div id="tahoma"> Keyword </div> </td>
		<td> <div id="tahoma"> : </div> </td>
		<td> <?php echo $form->input('keyword'); ?> </td>
	</tr>
	<tr>
		<td> <?php echo $form->end('Seacrh'); ?> </td>
	</tr>
</table>
<?php if(!empty($result)): ?><hr>
<h5> Hasil Pencarian: </h5>
<div id="tahoma">
<ol>
	<?php 
	foreach($result as $search): 
	?>
	
	<li>
		<?php echo $search['Search']['nim']; ?> |
		<?php echo $search['Search']['nama']; ?> |
		<?php echo $search['Search']['kota']; ?>
	</li><br>
	
	<?php endforeach; ?>
</ol>
</div>
<?php else: ?>
	<?php if($this->data): ?>
	<div id="tahoma"><hr> Maaf, Data Tidak Ditemukan </div>
	<?php endif; ?>
<?php endif; ?>