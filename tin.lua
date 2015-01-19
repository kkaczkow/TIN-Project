-- najprosciej dodac ten skrypt wywolujac wireszarka w nastepujacy sposob:
-- sudo wireshark -X lua_script:'/<home_dir>/tin.lua'

p_tindrive= Proto("tindrive", "TinDrive Protocol")
local data_data = Field.new("data.data")
local td_type = ProtoField.string("tindrive.type", "Packet type", ftypes.STRING)
local td_data = ProtoField.bytes("tindrive.data", "Packet data")
p_tindrive.fields._type = td_type
p_tindrive.fields._data = td_data

local ssl_dissector = Dissector.get("ssl")

function p_tindrive.dissector(buffer, pinfo, tree)
	ssl_dissector:call(buffer, pinfo, tree)
	pinfo.cols.protocol = "TIN"
	local subtree = tree:add(p_tindrive, buffer(), "TinDrive Protocol Data")
	if data_data() then
		local data_arr = { data_data() }
		local data = ByteArray.new()
		for k, v in ipairs(data_arr) do
			data:append(v())
		end
		local data_tvb = ByteArray.tvb(data, "TinDrive Raw Data")
		local packet_type = ""
		if data_tvb[1] == 0x01 then
			packet_type = 0x16
		elseif data_tvb[1] == 0x01 then
			packet_type = "REGISTER"
		elseif data_tvb[1] == 0x81 then
			packet_type = "REGISTERED_ID"
		elseif data_tvb[1] == 0x02 then
			packet_type = "SERVICES"
		elseif data_tvb[1] == 0x03 then
			packet_type = "LIST_AGENTS"
		elseif data_tvb[1] == 0x83 then
			packet_type = "AGENT_LIST"
		elseif data_tvb[1] == 0x04 then
			packet_type = "LIST_SERVICES"
		elseif data_tvb[1] == 0x84 then
			packet_type = "SERVICES_LIST"
		elseif data_tvb[1] == 0x05 then
			packet_type = "CONN_REQUEST"
		elseif data_tvb[1] == 0x85 then
			packet_type = "CONNECT"
		end
		subtree:add(td_type, packet_type)
		subtree:add(td_data, data_tvb())
	end
end

DissectorTable.get("tcp.port"):add(9000, p_tindrive)
