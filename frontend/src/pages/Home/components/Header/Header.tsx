import {Badge, Dropdown} from 'antd';
import type {MenuProps} from 'antd';
import {BellOutlined} from '@ant-design/icons';
import LogoDi2win from '../../../../../assets/d2win.svg';

export default function Header() {
    const errorList = [
        'Erro 1: Falha ao carregar dados.',
        'Erro 2: Conexão instável.',
        'Erro 3: Token expirado.',
    ];

    const items: MenuProps['items'] = errorList.map((err, i) => ({
        key: i.toString(),
        label: <span className="text-sm text-gray-700">{err}</span>,
    }));

    return (
        <header className="flex items-center justify-between px-6 py-4 bg-[#F31E8D] ">
            <div className="flex items-center gap-4">
                <img src={LogoDi2win} alt="Logo Di2win" className="h-10 w-auto"/>
            </div>
            <div className="relative hover:bg-black/20 rounded-full transition-colors duration-200 p-1">
                <Dropdown menu={{items}} placement="bottomRight" trigger={['click']}>
                    <Badge count={errorList.length} offset={[-5, 5]}>
                        <BellOutlined className="!text-white text-3xl cursor-pointer"/>
                    </Badge>
                </Dropdown>
            </div>
        </header>
    );
}
