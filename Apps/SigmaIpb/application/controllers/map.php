<?php
	class Map extends CI_Controller{
		function Map(){
			parent::__construct();
			session_start();
			$this->load->model('Query_model','qm', TRUE);	//	menghasilkan objek model dengan nama file model	
			$this->load->helper('fusioncharts'); //memanggil library untuk membuat grafik dari fusion chart
			$this->load->library('session');
		}
		
		public function index(){
			$this->load->view('map_view');		//pemuatan (load) atau pemanggilan suatu file yaitu map_view
		}
		
		public function coba(){
			$this->load->view('coba');
		}
		
		public function level2(){
			$this->load->view('level2');
		}
		
		public function level3(){
			$this->load->view('level3');
		}
		
		//----------------------- LEVEL 4 ----------------------------------
		public function level4(){
			$order = $this->input->post('order');
			if( $order !== false){
				if($order){
					$filter_session_data['order'] = $order;
				}
				else{
					$order = $this->session->userdata('order');
				}
				$data['order'] = $order;
				
				//save session data into the session
				if(isset($filter_session_data)){
					$this->session->set_userdata($filter_session_data);
				}
			}
			else{
				$filter_session_data['order'] = null;
				$this->session->set_userdata($filter_session_data);
				
				//pre selected options
				$data['search_string_selected'] = '';
				$data['order'] = 'id_barang';
				
				//fetch sql data into arrays
				$data['aset'] = $this->qm->get_aset('', '');
				
				if(isset($_SESSION['katakunci'])){
					$katakunci = $_SESSION['katakunci'];
					$berdasarkan = $_SESSION['berdasarkan'];
					
					//-------------------------- DB --------------------------------
					$data2 = $this->qm->get_cari4($katakunci, $berdasarkan);
					$marker = '';
					foreach($data2->result() as $row){
						$koordinat = $row->koordinat;
						$koordinat = str_replace('(','',$koordinat);
						$koordinat = str_replace(')','',$koordinat);
						if($koordinat != ''){
							$marker = $marker." var feature = new OpenLayers.Feature.Vector(
												new OpenLayers.Geometry.Point(".$koordinat.").transform('EPSG:4326', 'EPSG:3857'),
												{nama_barang: 'Nama Barang: ".$row->nama_barang."', ruang: 'Ruang: ".$row->nama_ruang."', foto: '$row->foto'},
												{externalGraphic: '../images/icon.png', graphicHeight: 25, graphicWidth: 15, graphicXOffset:-7.5, graphicYOffset:-25 }
												);
												vectorLayer.addFeatures(feature);";
						}
					}
					//------------------------- END DB -------------------------------
					
					$data['marker'] = $marker;
					
					$data['titik']= $this->qm->get_cari4($katakunci, $berdasarkan);
					$data['lvl5']= $this->qm->get_cari($katakunci, $berdasarkan);
					$data['lvl6']= $this->qm->get_cari6($katakunci, $berdasarkan);
					$_SESSION['jum5'] = $data['lvl5']->num_rows;
					$_SESSION['jum4'] = $data['titik']->num_rows;
					$_SESSION['jum6'] = $data['lvl6']->num_rows;
					$data['wing']= $this->qm->get_wing4($katakunci, $berdasarkan);
				}
				else{
					$data['marker'] = '';
				}
				
				//load the view
				$this->load->view('level4', $data);
			}
		}
		
		public function marker4(){
			$_SESSION['katakunci'] = $this->input->post('katakunci');
			$_SESSION['berdasarkan'] = $this->input->post('berdasarkan');
			redirect('map/level4');
		}
		
		public function reset_cari4(){
			session_destroy();
			redirect('map/level4','refresh');
		}
		//----------------------- END LEVEL 4 ----------------------------------
		
		//----------------------- LEVEL 5 ----------------------------------
		public function level5(){
			$order = $this->input->post('order');
			if( $order !== false){
				if($order){
					$filter_session_data['order'] = $order;
				}
				else{
					$order = $this->session->userdata('order');
				}
				$data['order'] = $order;
				
				//save session data into the session
				if(isset($filter_session_data)){
					$this->session->set_userdata($filter_session_data);
				}
			}
			else{
				$filter_session_data['order'] = null;
				$this->session->set_userdata($filter_session_data);
				
				//pre selected options
				$data['search_string_selected'] = '';
				$data['order'] = 'id_barang';
				
				//fetch sql data into arrays
				$data['aset'] = $this->qm->get_aset('', '');
				
				if(isset($_SESSION['katakunci'])){
					$katakunci = $_SESSION['katakunci'];
					$berdasarkan = $_SESSION['berdasarkan'];
					
					//----------------------------DB----
					$data2 = $this->qm->get_cari($katakunci, $berdasarkan);
					$marker = '';
					foreach($data2->result() as $row){
						$koordinat = $row->koordinat;
						$koordinat = str_replace('(','',$koordinat);
						$koordinat = str_replace(')','',$koordinat);
						if($koordinat != ''){
							$marker = $marker." var feature = new OpenLayers.Feature.Vector(
												new OpenLayers.Geometry.Point(".$koordinat.").transform('EPSG:4326', 'EPSG:3857'),
												{nama_barang: 'Nama Barang: ".$row->nama_barang."', ruang: 'Ruang: ".$row->nama_ruang."', foto: '$row->foto'},
												{externalGraphic: '../images/icon.png', graphicHeight: 25, graphicWidth: 15, graphicXOffset:-7.5, graphicYOffset:-25 }
												);
												vectorLayer.addFeatures(feature);";
						}
					}
					//----------end DB----------
					
					$data['marker'] = $marker;
					
					$data['titik']= $this->qm->get_cari($katakunci, $berdasarkan);
					$data['lvl4']= $this->qm->get_cari4($katakunci, $berdasarkan);
					$data['lvl6']= $this->qm->get_cari6($katakunci, $berdasarkan);
					$_SESSION['jum5'] = $data['titik']->num_rows;
					$_SESSION['jum4'] = $data['lvl4']->num_rows;
					$_SESSION['jum6'] = $data['lvl6']->num_rows;
					$data['wing']= $this->qm->get_wing($katakunci, $berdasarkan);
				}
				else{
					$data['marker'] = '';
				}
				
				//load the view
				$this->load->view('level5', $data);
			}
		}
		
		public function marker5(){
			$_SESSION['katakunci'] = $this->input->post('katakunci');
			$_SESSION['berdasarkan'] = $this->input->post('berdasarkan');
			redirect('map/level5');
		}
		
		public function reset_cari5(){
			session_destroy();
			redirect('map/level5','refresh');
		}
		//----------------------- END LEVEL 5 ----------------------------------
		
		//----------------------- LEVEL 6 ----------------------------------
		public function level6(){
			$order = $this->input->post('order'); 
			if( $order !== false){
				if($order){
					$filter_session_data['order'] = $order;
				}
				else{
					$order = $this->session->userdata('order');
				}
				$data['order'] = $order;
				
				//save session data into the session
				if(isset($filter_session_data)){
					$this->session->set_userdata($filter_session_data);
				}
			}
			else{
				$filter_session_data['order'] = null;
				$this->session->set_userdata($filter_session_data);
				
				//pre selected options
				$data['search_string_selected'] = '';
				$data['order'] = 'id_barang';
				
				//fetch sql data into arrays
				$data['aset'] = $this->qm->get_aset('','');
				
				if(isset($_SESSION['katakunci'])){
					$katakunci = $_SESSION['katakunci'];
					$berdasarkan = $_SESSION['berdasarkan'];
					
					//----------------------------DB----
					$data2 = $this->qm->get_cari6($katakunci, $berdasarkan);
					$marker = '';
					foreach($data2->result() as $row){
						$koordinat = $row->koordinat;
						$koordinat = str_replace('(','',$koordinat);
						$koordinat = str_replace(')','',$koordinat);
						if($koordinat != ''){
							$marker = $marker." var feature = new OpenLayers.Feature.Vector(
												new OpenLayers.Geometry.Point(".$koordinat.").transform('EPSG:4326', 'EPSG:3857'),
												{nama_barang: 'Nama Barang: ".$row->nama_barang."', ruang: 'Ruang: ".$row->nama_ruang."', foto: '$row->foto'},
												{externalGraphic: '../images/icon.png', graphicHeight: 25, graphicWidth: 15, graphicXOffset:-7.5, graphicYOffset:-25 }
												);
												vectorLayer.addFeatures(feature);";
						}
					}
					//----------end DB----------
					
					$data['marker'] = $marker;
					
					$data['titik']= $this->qm->get_cari6($katakunci, $berdasarkan);
					$data['lvl4']= $this->qm->get_cari4($katakunci, $berdasarkan);
					$data['lvl5']= $this->qm->get_cari($katakunci, $berdasarkan);
					$_SESSION['jum5'] = $data['lvl5']->num_rows;
					$_SESSION['jum4'] = $data['lvl4']->num_rows;
					$_SESSION['jum6'] = $data['titik']->num_rows;
					$data['wing']= $this->qm->get_wing6($katakunci, $berdasarkan);
				}
				else{
					$data['marker'] = '';
				}
				
				//load the view
				$this->load->view('level6', $data);
			}
		}
		
		public function marker6(){
			$_SESSION['katakunci'] = $this->input->post('katakunci');
			$_SESSION['berdasarkan'] = $this->input->post('berdasarkan');
			redirect('map/level6');
		}
		
		public function reset_cari6(){
			session_destroy();
			redirect('map/level6','refresh');
		}
		//----------------------- END LEVEL 6 ----------------------------------
		
		//----------------------- MANIPULASI ----------------------------------
		public function manajemen(){
			$this->load->view('manajemen');
		}
		
		function add(){
			$this->form_validation->set_rules('kode','kode','required');
			$this->form_validation->set_rules('nama','nama','required');
			
			$config['upload_path'] = 'images/uploads/';
			$config['allowed_types'] = 'gif|jpg|png';
			$config['max_size']	= '5000';
			$config['max_width']  = '5000';
			$config['max_height']  = '5000';
			
			$this->load->library('upload', $config);
			
			$koordinat = $this->input->post('longitude').','.$this->input->post('latitude');
			
			if($this->form_validation->run() == TRUE){
				if(!empty($_FILES["upload"]["tmp_name"])){
					if(! $this->upload->do_upload("upload")){
						$this->session->set_flashdata('message','Gagal menambah data aset');
					} else {
						//upload the new image
						$data = $this->upload->data("upload");
						$link = base_url().'images/uploads/'.$data['file_name'];

						if($_POST){
							if($this->input->post('longitude')){
								$d['kode_barang'] = $this->input->post('kode');
								$d['nama_barang'] = $this->input->post('nama');
								$d['merk_type'] = $this->input->post('merk');
								$d['tahun'] = $this->input->post('tahun');
								$d['harga_satuan'] = $this->input->post('harga');
								$d['sumber_dana'] = $this->input->post('dana');
								$d['kode_lokasi'] = $this->input->post('ruang');
								$d['koordinat'] = $koordinat;
								$d['foto'] = $link;
							} else {
								$d['kode_barang'] = $this->input->post('kode');
								$d['nama_barang'] = $this->input->post('nama');
								$d['merk_type'] = $this->input->post('merk');
								$d['tahun'] = $this->input->post('tahun');
								$d['harga_satuan'] = $this->input->post('harga');
								$d['sumber_dana'] = $this->input->post('dana');
								$d['kode_lokasi'] = $this->input->post('ruang');
								$d['koordinat'] = NULL;
								$d['foto'] = $link;
							}
						}
					}
				} else {
					if($_POST){
						if($this->input->post('longitude')){
							$d['kode_barang'] = $this->input->post('kode');
							$d['nama_barang'] = $this->input->post('nama');
							$d['merk_type'] = $this->input->post('merk');
							$d['tahun'] = $this->input->post('tahun');
							$d['harga_satuan'] = $this->input->post('harga');
							$d['sumber_dana'] = $this->input->post('dana');
							$d['kode_lokasi'] = $this->input->post('ruang');
							$d['koordinat'] = $koordinat;
							$d['foto'] = NULL;
						} else {
							$d['kode_barang'] = $this->input->post('kode');
							$d['nama_barang'] = $this->input->post('nama');
							$d['merk_type'] = $this->input->post('merk');
							$d['tahun'] = $this->input->post('tahun');
							$d['harga_satuan'] = $this->input->post('harga');
							$d['sumber_dana'] = $this->input->post('dana');
							$d['kode_lokasi'] = $this->input->post('ruang');
							$d['koordinat'] = NULL;
							$d['foto'] = NULL;
						}
					}
				}
				
				$this->qm->addAset($d);
				$this->session->set_flashdata('message','Berhasil menambah data aset');
				redirect('map/manajemen','refresh');
			}
			$data['title'] = "Tambah Data Aset";
			$this->load->view('manajemen', $data);
		}
		
		public function edit(){
			$order = $this->input->post('order'); 
			if( $order !== false){
				if($order){
					$filter_session_data['order'] = $order;
				}
				else {
					$order = $this->session->userdata('order');
				}
				$data['order'] = $order;
				
				//save session data into the session
				if(isset($filter_session_data)){
					$this->session->set_userdata($filter_session_data);
				}
			}
			else{
				$filter_session_data['order'] = null;
				$this->session->set_userdata($filter_session_data);
				
				//pre selected options
				$data['search_string_selected'] = '';
				$data['order'] = 'id_barang';
				
				//fetch sql data into arrays
				$data['aset'] = $this->qm->get_aset('', '');
				
				//load the view
				$this->load->view('edit', $data);
			}
		}
		
		function editas($id = null){
			$this->form_validation->set_rules('kode','kode','required');
			$this->form_validation->set_rules('nama','nama','required');
			
			$config['upload_path'] = 'images/uploads/';
			$config['allowed_types'] = 'gif|jpg|png';
			$config['max_size']	= '5000';
			$config['max_width']  = '5000';
			$config['max_height']  = '5000';
			
			$this->load->library('upload', $config);
			
			$koordinat = $this->input->post('longitude').','.$this->input->post('latitude');
			
			if($this->form_validation->run() == TRUE){
				if(!empty($_FILES["upload"]["tmp_name"])){
					if(! $this->upload->do_upload("upload")){
						$this->session->set_flashdata('message','Gagal menambah data aset');
					} else {
						//upload the new image
						$data = $this->upload->data("upload");
						$link = base_url().'images/uploads/'.$data['file_name'];

						if($_POST){
							if($this->input->post('longitude')){
								$d['id'] = $this->input->post('ida');
								$d['kode_barang'] = $this->input->post('kode');
								$d['nama_barang'] = $this->input->post('nama');
								$d['merk_type'] = $this->input->post('merk');
								$d['tahun'] = $this->input->post('tahun');
								$d['harga_satuan'] = $this->input->post('harga');
								$d['sumber_dana'] = $this->input->post('dana');
								$d['kode_lokasi'] = $this->input->post('ruang');
								$d['koordinat'] = $koordinat;
								$d['foto'] = $link;
							} else {
								$d['id'] = $this->input->post('ida');
								$d['kode_barang'] = $this->input->post('kode');
								$d['nama_barang'] = $this->input->post('nama');
								$d['merk_type'] = $this->input->post('merk');
								$d['tahun'] = $this->input->post('tahun');
								$d['harga_satuan'] = $this->input->post('harga');
								$d['sumber_dana'] = $this->input->post('dana');
								$d['kode_lokasi'] = $this->input->post('ruang');
								$d['koordinat'] = NULL;
								$d['foto'] = $link;
							}
						}
					}
				} else {
					if($_POST){
						if($this->input->post('longitude')){
							$d['id'] = $this->input->post('ida');
							$d['kode_barang'] = $this->input->post('kode');
							$d['nama_barang'] = $this->input->post('nama');
							$d['merk_type'] = $this->input->post('merk');
							$d['tahun'] = $this->input->post('tahun');
							$d['harga_satuan'] = $this->input->post('harga');
							$d['sumber_dana'] = $this->input->post('dana');
							$d['kode_lokasi'] = $this->input->post('ruang');
							$d['koordinat'] = $koordinat;
							$d['foto'] = NULL;
						} else {
							$d['id'] = $this->input->post('ida');
							$d['kode_barang'] = $this->input->post('kode');
							$d['nama_barang'] = $this->input->post('nama');
							$d['merk_type'] = $this->input->post('merk');
							$d['tahun'] = $this->input->post('tahun');
							$d['harga_satuan'] = $this->input->post('harga');
							$d['sumber_dana'] = $this->input->post('dana');
							$d['kode_lokasi'] = $this->input->post('ruang');
							$d['koordinat'] = NULL;
							$d['foto'] = NULL;
						}
					}
				}
				$this->qm->editAset($d);
				$this->session->set_flashdata('message','Data aset berhasil diubah');
				redirect('index.php/map/editas/'.$d['id'],'refresh');
			}
			$data['title'] = "Edit Data";
			$data['aset'] = $this->qm->getAset($id);
			$this->load->view('editaset',$data);
		}
		
		
		
		
	
		public function pencarianhEdt(){
			$katakunci = $this->input->post('katakunci');
			$berdasarkan = $this->input->post('berdasarkan');
			$data['titik']= $this->qm->get_cariEdit($katakunci, $berdasarkan);
			$this->load->view('tabel_view2Edt',$data);
		}
		
		public function pencarianEdt(){
			$katakunci = $this->input->post('katakunci');
			$berdasarkan = $this->input->post('berdasarkan');
			$data['titik']= $this->qm->get_cariEdit($katakunci, $berdasarkan);
			$data['wing']= $this->qm->get_wing($katakunci, $berdasarkan);
			$this->load->view('tabel_viewEdt',$data);
		}
		
		public function tambahaset(){
			$kode = $this->input->post('kode');
			$nama = $this->input->post('nama');
			$merk = $this->input->post('merk');
			$tahun = $this->input->post('tahun');
			$harga = $this->input->post('harga');
			$dana = $this->input->post('dana');
			$this->qm->tambah_aset($kode, $nama, $merk, $tahun, $harga, $dana);
			$this->load->view('level5');
		}
		
		public function getLokasiAset(){
			$data['aset']=$this->qm->get_aset();
			$this->load->view('pilih_aset',$data);
		}
		
		
		
		function delete($id){
			if(!empty($id)){
				$this->qm->deleteAset($id);
				$this->session->set_flashdata('message','Pegawai Data has been DELETE !');
				redirect('map/edit','refresh');
			}
		}
		
		public function get_lokasi(){
			$data['lok']=$this->qm->get_lokasi();
			$this->load->view('manajemen',$data);
		}
		
		public function get_lokasis(){
			$nama=$this->input->post('id');
			$lok=$this->qm->get_lokasi_by_nama($nama);
			echo '<select name="lok" id="lok">';
			foreach($lok->result() as $k){
				echo '<option value="'.$k->kode_lokasi.'">'.$k->nama_ruang.'</option>';		//tampilkan hasi query
			}
			echo '</select> ';
		}
	}
?>