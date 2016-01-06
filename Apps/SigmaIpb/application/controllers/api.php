<?php
	class Api extends CI_Controller{
		function Api(){
			parent::__construct();
			$this->load->model('Query_model','qm', TRUE);	//	menghasilkan objek model dengan nama file model	
			$this->load->helper('fusioncharts'); //memanggil library untuk membuat grafik dari fusion chart
			$this->load->library('session');
		}
		
		public function get_asset($param='', $offet=1, $limit=10){
			$offset = $limit*$offet - $limit;
			$asset['total'] = $this->qm->count_aset($param);
			$asset['data'] = $this->qm->get_aset($param, 'nama_barang', 'asc', $limit, $offset);		
			//echo "<pre>"; print_r($asset);
			echo json_encode($asset);
		}
		
		public function get_lokasi($param='', $limit=10){
			$return = [];
			if($param == '')
				$lokasi = $this->qm->getLokasiAset();
			else
				$lokasi = $this->qm->get_lokasi_by_nama($param);
			
			$result['lokasi'] = $lokasi->result();
			foreach( $result['lokasi'] as $key => $val ){
				$return['lokasi'][]['kode'] = $val->kode_lokasi . ' | ' . $val->nama_ruang;
			}
			echo json_encode($return);
		}
		
		public function add(){
			if( ! $this->input->post() ){
				$this->output->set_content_type('application/json')->set_output(json_encode(array('error' => "You don't have any access to this path")));
				echo $this->output->get_output();
				exit;
			}
			
			$this->load->helper('file');
			$data_simpan = json_encode($_POST);
			if( ! write_file('./file.json', $data_simpan)){
				 echo 'Unable to write the file';
			}else{
				echo 'File written!';
			}
			
			$_POST['koordinat'] = $this->input->post('longitude').','.$this->input->post('latitude');
			unset($_POST['longitude'], $_POST['latitude']);
			
			$data_simpan = json_encode($_POST);
			if( ! write_file('./post_baru.json', $data_simpan) ){
				 echo 'Unable to write the file';
			}else{
				echo 'File written!';
			}
			
			$config['upload_path'] = 'images/uploads/';
			$config['allowed_types'] = '*';
			$config['max_size']	= '5000';
			$config['max_width']  = '5000';
			$config['max_height']  = '5000';
			
			$this->load->library('upload', $config);
			
			if(! $this->upload->do_upload('UploadFile')){
				$data_simpan = [];
				$data_simpan['files'] = $_FILES;
				$error = array('error' => $this->upload->display_errors());
				$data_simpan['error'] = $error;
				$data_simpan = json_encode($data_simpan);
				if( ! write_file('./error_upload.json', $data_simpan)){
				 echo 'Unable to write the file';
				}else{
					echo 'File written!';
				}
			} else {
				//upload the new image
				$data = $this->upload->data();
				$_POST['foto'] = base_url().'images/uploads/'.$data['file_name'];
				/* Build data ID Lokasi */
					$string = explode('|', $this->input->post('kode_lokasi'));
					$_POST['kode_lokasi'] = ltrim(rtrim(@$string[0]));
				if( $this->qm->addAset( $this->input->post() ) ){
					write_file('./final_sukses.json', json_encode($_POST));
				}else{
					write_file('./final_gagal.json', json_encode($_POST));
				}
			}
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
	}
?>