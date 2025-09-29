import {} from "react";
import { Space, Button, Dropdown, message } from "antd";
import type { MenuProps } from "antd";
import {
  CloudDownloadOutlined,
  FilePdfOutlined,
  FileExcelOutlined,
} from "@ant-design/icons";

const handleMenuClick: MenuProps["onClick"] = (e) => {
  message.info("Click on menu item.");
  console.log("click", e);
};

const items: MenuProps["items"] = [
  {
    label: "PDF",
    key: "1",
    icon: <FilePdfOutlined />,
  },
  {
    label: "CSV",
    key: "2",
    icon: <FileExcelOutlined />,
  },
];

const menuProps = {
  items,
  onClick: handleMenuClick,
};

export default function DownloadButton() {
  return (
    <Dropdown menu={menuProps}>
      <Button
        type="primary"
        className="!bg-[#F31E8D] !border-none !rounded-md hover:!bg-[#F31E8D] focus:!bg-[#F31E8D] focus:!ring-pink-300"
      >
        <Space>
          Baixar
          <CloudDownloadOutlined />
        </Space>
      </Button>
    </Dropdown>
  );
}
