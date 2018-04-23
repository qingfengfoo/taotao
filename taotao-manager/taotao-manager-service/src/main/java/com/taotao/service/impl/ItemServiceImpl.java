package com.taotao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemService;
/**
 * 商品查询service
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//分页处理
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example=new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example); 
		//取分页信息
		PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(list);
		//返回处理结果
		EasyUIDataGridResult dataGridResult=new EasyUIDataGridResult();
		dataGridResult.setTotal(pageInfo.getTotal());
		dataGridResult.setRows(list);
		return dataGridResult;
	}
	
	@Override
	public TbItem getItemById(Long itemId) {
//		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example=new TbItemExample();
		//创建查询条件
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		TbItem item=null;
		//判断list是否为空
		if(list!=null&&list.size()>0){
			item = list.get(0);
		}
		return item ;
	}
	@Override
	public TaotaoResult createItem(TbItem item, String desc,String itemParam) {
		//生成商品Id
		long itemId = IDUtils.genItemId();
		//补全TbItem属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		//创建时间和更新时间
		Date date=new Date();
		item.setCreated(date);
		item.setUpdated(date);
		//插入商品表
		itemMapper.insert(item);
		//商品描述
		TbItemDesc tbItemDesc=new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		//插入商品描述数据
		itemDescMapper.insert(tbItemDesc);
		
		//添加商品规格参数处理
		TbItemParamItem tbItemParamItem=new TbItemParamItem();
		tbItemParamItem.setItemId(itemId);
		tbItemParamItem.setParamData(itemParam);
		tbItemParamItem.setCreated(date);
		tbItemParamItem.setUpdated(date);
		//插入数据
		tbItemParamItemMapper.insert(tbItemParamItem);
		 
		return TaotaoResult.ok();
	}

	@Override
	public String getItemParamHtml(Long itemId) {
		// 根据商品ID查询规格参数
		TbItemParamItemExample example=new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		//执行查询
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		if(list==null || list.isEmpty()){
			return "";
		}
		//取规格参数
		TbItemParamItem tbItemParamItem = list.get(0);
		//取json数据
		String paramData = tbItemParamItem.getParamData();
		//转换成java对象
		List<Map> mapList = JsonUtils.jsonToList(paramData, Map.class);
		//遍历list 生成html
		StringBuffer sb=new StringBuffer();
		
		sb.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"1\">\n");
		for (Map map : mapList) {
			sb.append("	<tr>\n" );
			sb.append("		<th>"+map.get("group")+"</th>\n" );
			sb.append("	</tr>\n" );
			//取规格项
			List<Map> mapList2=(List<Map>) map.get("params");
			for (Map map2 : mapList2) {
				sb.append("	<tr>\n" );
				sb.append("		<td>"+map2.get("k")+"</td>\n" );
				sb.append("		<td>"+map2.get("v")+"</td>\n" );
				sb.append("	</tr>\n" );
			}
			
		}
		sb.append("</table>");
		
		return sb.toString();
	}

}
